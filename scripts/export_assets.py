from models import Asset, session
import os


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
