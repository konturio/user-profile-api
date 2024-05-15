from sqlalchemy import create_engine, Column, Integer, String, LargeBinary, ForeignKey, UniqueConstraint, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
from datetime import datetime
import os

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
    return session.query(Asset).filter(Asset.media_type == 'text',Asset.media_subtype == 'markdown', Asset.language == 'en').all()

def extract_markdown_content(markdown):
    return markdown

def export_to_pot_file(translatable_strings, file_path):
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('msgid ""\n')
        f.write('msgstr ""\n')
        f.write('"Content-Type: text/plain; charset=UTF-8\\n"\n')
        f.write('"Language: en\\n"\n\n')

        for string in translatable_strings:
            escaped_string = string.replace('\n', '\\n').replace('"', '\\"')
            f.write(f'msgid "{escaped_string}"\n')
            f.write('msgstr ""\n\n')

def export_markdown_assets_to_gettext(output_file):
    assets = get_english_markdown_assets()
    translatable_strings = []

    for asset in assets:
        markdown = asset.asset.decode('utf-8')
        translatable_strings.append(extract_markdown_content(markdown))

    export_to_pot_file(translatable_strings, output_file)

# Specify the output file and run the export
output_file = 'i18n/template/assets.pot'
export_markdown_assets_to_gettext(output_file)
