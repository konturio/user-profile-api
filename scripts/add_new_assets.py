import os
from sqlalchemy.exc import IntegrityError
import mimetypes
from models import Asset, App, Feature, session


def get_or_create_model_id(model, name):
    name = str(name)
    m = session.query(model).filter_by(name=name).first()
    if m is None:
        m = model(name=name)
        session.add(m)
        session.commit()
    return m.id


def get_app_id(app_name):
    return get_or_create_model_id(model=App, name=app_name)


def get_feature_id(feature_name):
    return get_or_create_model_id(model=Feature, name=feature_name)


def add_or_update_asset(file_path, app_id, feature_id, language, description, owner_user_id):
    filename = os.path.basename(file_path)
    mime_type = mimetypes.guess_type(file_path)[0]

    if mime_type:
        media_type, media_subtype = mime_type.split('/')
    else:
        if filename.lower().endswith('.md'):
            media_type, media_subtype = 'text', 'markdown'
        else:
            media_type, media_subtype = 'undefined', 'undefined'

    with open(file_path, 'rb') as file:
        asset_data = file.read()

    existing_asset = session.query(Asset).filter_by(
        app_id=app_id,
        feature_id=feature_id,
        filename=filename,
        language=language
    ).first()

    if existing_asset:
        if existing_asset.asset != asset_data:
            existing_asset.asset = asset_data
            session.commit()
            print(f'Updated {filename} in the database.')
        else:
            print(f'No changes detected for {filename}. Skipping update.')
    else:
        new_asset = Asset(
            media_type=media_type,
            media_subtype=media_subtype,
            filename=filename,
            description=description,
            owner_user_id=owner_user_id,
            language=language,
            app_id=app_id,
            feature_id=feature_id,
            asset=asset_data
        )
        session.add(new_asset)
        print(f'Added {filename} to the database.')

    try:
        session.commit()
    except IntegrityError as e:
        print(e)
        # do SELECT setval('assets_id_seq', (SELECT MAX(id) FROM assets), true) if PK violation
        session.rollback()
        print(f'Skipped {filename}: failed to add or update in database.')


def process_assets_in_directory(directory):
    for app_dir in filter(lambda e: e.is_dir(), os.scandir(directory)):
        app_id = get_app_id(app_dir.name)
        for feature_dir in filter(lambda e: e.is_dir(), os.scandir(app_dir.path)):
            feature_id = get_feature_id(feature_dir.name)
            for language_dir in filter(lambda e: e.is_dir(), os.scandir(feature_dir.path)):
                language = language_dir.name
                for file in filter(lambda e: e.is_file(), os.scandir(language_dir.path)):
                    description = "Asset uploaded via script"
                    owner_user_id = None
                    add_or_update_asset(file.path, app_id, feature_id, language, description, owner_user_id)

# Specify the output directory and start the export
assets_directory = 'assets'
process_assets_in_directory(assets_directory)
