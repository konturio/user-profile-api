import os
from sqlalchemy import create_engine, Column, Integer, String, LargeBinary, ForeignKey, UniqueConstraint, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
from babel.messages.pofile import read_po
from babel.messages.catalog import Catalog
from datetime import datetime
import tempfile

Base = declarative_base()

class App(Base):
    __tablename__ = 'app'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String, nullable=False)

class Feature(Base):
    __tablename__ = 'feature'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String, nullable=False)

class Asset(Base):
    __tablename__ = 'assets'
    __table_args__ = (UniqueConstraint('app_id', 'filename', 'language', name='unique_asset'),)

    id = Column(Integer, primary_key=True, autoincrement=True)
    media_type = Column(String, nullable=False)
    media_subtype = Column(String, nullable=False)
    filename = Column(String, nullable=False)
    description = Column(String)
    owner_user_id = Column(Integer)
    language = Column(String)
    last_updated = Column(DateTime, nullable=False, default=datetime.utcnow)
    app_id = Column(Integer, ForeignKey('app.id'))
    feature_id = Column(Integer, ForeignKey('feature.id'))
    asset = Column(LargeBinary, nullable=False)

    app = relationship("App")
    feature = relationship("Feature")

# Replace with your actual database URL
DATABASE_URL = 'postgresql://localhost:54321/user-profile-api-db?password=UDeH2l25WME1V1tnIS0LTDbL&user=user-profile-api'

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()

def get_english_markdown_assets():
    return session.query(Asset).filter(Asset.media_type == 'text', Asset.media_subtype == 'markdown', Asset.language == 'en').all()

def preprocess_po_file(file_path):
    # Read the content of the file
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    # Write the content to a temporary file excluding problematic headers
    with tempfile.NamedTemporaryFile(delete=False, mode='w', encoding='utf-8') as temp_file:
        for line in lines:
            if not (line.startswith('"Creation-Date:') or line.startswith('"POT-Creation-Date:') or line.startswith('"PO-Revision-Date:')):
                temp_file.write(line)
        temp_file_path = temp_file.name

    return temp_file_path

def read_po_file(file_path):
    processed_file_path = preprocess_po_file(file_path)  # Preprocess the file to remove problematic headers
    with open(processed_file_path, 'rb') as f:
        catalog = read_po(f)
    os.remove(processed_file_path)  # Clean up the temporary file
    return catalog

# Function to add or update translations in the database
def add_or_update_translations(session, translations, language):
    english_assets = get_english_markdown_assets()

    for asset in english_assets:
        markdown = asset.asset.decode('utf-8')
        if markdown in translations:
            translated_text = translations[markdown]
            existing_asset = session.query(Asset).filter_by(filename=asset.filename, language=language).first()
            if existing_asset:
                existing_asset.asset = translated_text.encode('utf-8')
                existing_asset.last_updated = datetime.utcnow()
            else:
                new_asset = Asset(
                    media_type=asset.media_type,
                    media_subtype=asset.media_subtype,
                    filename=asset.filename,
                    description=asset.description,
                    owner_user_id=asset.owner_user_id,
                    language=language,
                    app_id=asset.app_id,
                    feature_id=asset.feature_id,
                    asset=translated_text.encode('utf-8'),
                    last_updated=datetime.utcnow()
                )
                session.add(new_asset)

    session.commit()

# Path to the i18n directory
i18n_directory = 'i18n'

# Process each language directory
for language_dir in os.listdir(i18n_directory):
    language_path = os.path.join(i18n_directory, language_dir)
    if os.path.isdir(language_path):
        po_file_path = os.path.join(language_path, 'assets.po')
        if os.path.isfile(po_file_path):
            catalog = read_po_file(po_file_path)
            translations = {message.id: message.string for message in catalog if message.string}
            add_or_update_translations(session, translations, language_dir)
