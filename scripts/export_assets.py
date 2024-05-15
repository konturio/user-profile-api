from sqlalchemy import create_engine, Column, Integer, String, LargeBinary, ForeignKey, UniqueConstraint, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
from datetime import datetime
import os

# Определение моделей (повторно используем классы из предыдущего скрипта)
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

# Подключение к базе данных
DATABASE_URL = 'postgresql://localhost:54321/user-profile-api-db?password=UDeH2l25WME1V1tnIS0LTDbL&user=user-profile-api'

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()

def export_assets(output_directory):
    # Создать выходную директорию, если она не существует
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    # Извлечь все ассеты из базы данных
    assets = session.query(Asset).all()

    for asset in assets:
        # Построить путь для сохранения файла
        app_name = asset.app.name
        feature_name = asset.feature.name
        language = asset.language
        filename = asset.filename

        directory_path = os.path.join(output_directory, app_name, feature_name, language)
        if not os.path.exists(directory_path):
            os.makedirs(directory_path)

        file_path = os.path.join(directory_path, filename)

        # Сохранить ассет в файл
        with open(file_path, 'wb') as file:
            file.write(asset.asset)

        print(f'Asset exported to {file_path}')

# Задать выходную директорию и запустить экспорт
output_directory = 'assets'
export_assets(output_directory)
