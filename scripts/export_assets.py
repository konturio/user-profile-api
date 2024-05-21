from models import Asset, session
import os


def export_assets(output_directory):
    # Create the output directory if it does not exist
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    # Extract all assets from the database
    assets = session.query(Asset).all()

    for asset in assets:
        # Build the path for saving the file
        app_name = asset.app.name
        feature_name = asset.feature.name
        language = asset.language
        filename = asset.filename

        directory_path = os.path.join(output_directory, app_name, feature_name, language)
        if not os.path.exists(directory_path):
            os.makedirs(directory_path)

        file_path = os.path.join(directory_path, filename)

        # Check if the file exists
        if not os.path.exists(file_path):
            # Save the asset to the file if the file does not exist
            with open(file_path, 'wb') as file:
                file.write(asset.asset)
            print(f'Asset exported to {file_path}')
        else:
            print(f'File {file_path} already exists. Skipping export.')

# Specify the output directory and start the export
output_directory = 'assets'
export_assets(output_directory)
