import os
from sqlalchemy import create_engine, Column, Integer, String, LargeBinary, ForeignKey, UniqueConstraint, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
from sqlalchemy.exc import IntegrityError
from datetime import datetime
import mimetypes
from models import Asset, App, Feature
from config import DATABASE_URL

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()


def get_app_id(app_name):
    app = session.query(App).filter_by(name=app_name).first()
    if app is None:
        app = App(name=app_name)
        session.add(app)
        session.commit()
    return app.id


def get_feature_id(feature_name):
    feature = session.query(Feature).filter_by(name=feature_name).first()
    if feature is None:
        feature = Feature(name=feature_name)
        session.add(feature)
        session.commit()
    return feature.id


def add_asset_to_db(file_path, app_name, feature_name, language, description, owner_user_id):
    app_id = get_app_id(app_name)
    feature_id = get_feature_id(feature_name)

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
    for root, dirs, files in os.walk(directory):
        for file in files:
            file_path = os.path.join(root, file)
            path_parts = root.split(os.path.sep)
            if len(path_parts) >= 3:
                app_name = path_parts[-3]
                feature_name = path_parts[-2]
                language = path_parts[-1]
                description = "Example description"  # Modify as needed
                owner_user_id = 1  # Modify as needed

                add_asset_to_db(file_path, app_name, feature_name, language, description, owner_user_id)

# Задать выходную директорию и запустить экспорт
assets_directory = 'assets'
process_assets_in_directory(assets_directory)
