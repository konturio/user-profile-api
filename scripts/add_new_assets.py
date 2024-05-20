import os
from sqlalchemy.exc import IntegrityError
from datetime import datetime
import mimetypes
from models import Asset, App, Feature, session


def get_app_id(app_name):
    app = session.query(App).filter_by(name=str(app_name)).first()
    if app is None:
        app = App(name=str(app_name))
        session.add(app)
        session.commit()
    return app.id


def get_feature_id(feature_name):
    feature = session.query(Feature).filter_by(name=str(feature_name)).first()
    if feature is None:
        feature = Feature(name=str(feature_name))
        session.add(feature)
        session.commit()
    return feature.id


def add_asset_to_db(file_path, app_id, feature_id, language, description, owner_user_id):
    filename = os.path.basename(file_path)
    mime_type = mimetypes.guess_type(file_path)[0]

    if mime_type:
        media_type, media_subtype = mime_type.split('/')
    else:
        # Специальная обработка для файлов Markdown
        if filename.lower().endswith('.md'):
            media_type, media_subtype = 'text', 'markdown'
        else:
            # Если не удается определить mime-тип, используем "application/octet-stream"
            media_type, media_subtype = 'application', 'octet-stream'

    with open(file_path, 'rb') as file:
        asset_data = file.read()

    new_asset = Asset(
        media_type=media_type,
        media_subtype=media_subtype,
        filename=filename,
        description=description,
        owner_user_id=owner_user_id,
        language=language,
        app_id=app_id,
        feature_id=feature_id,
        asset=asset_data,
        last_updated=datetime.utcnow()
    )

    try:
        session.add(new_asset)
        session.commit()
        print(f'Added {filename} to database.')
    except IntegrityError:
        session.rollback()
        print(f'Skipped {filename}: already exists in database.')


def process_assets_in_directory(directory):
    for app_dir in os.scandir(directory):
        if app_dir.is_dir():
            app_id = get_app_id(app_dir.name)
            for feature_dir in os.scandir(app_dir.path):
                if feature_dir.is_dir():
                    feature_id = get_feature_id(feature_dir.name)
                    for language_dir in os.scandir(feature_dir.path):
                        if language_dir.is_dir():
                            language = language_dir.name
                            for file in os.scandir(language_dir.path):
                                if file.is_file():
                                    description = "Example description"
                                    owner_user_id = 1
                                    add_asset_to_db(file.path, app_id, feature_id, language, description, owner_user_id)

# Задать выходную директорию и запустить экспорт
assets_directory = 'assets'
process_assets_in_directory(assets_directory)
