# User Profile API - Scripts Manual

### Overview 

This manual provides detailed instructions on managing `assets` table within our ecosystem. It covers the creation, structuring, and automation of assets and translations.

Here is the structure of `assets` table:
- `media_type`: type of media (e.g., text, image) 
- `media_subtype`: specific subtype (e.g., markdown, jpeg)
- `file_name`: name of the file
- `description`: description of the file’s content (added manually) 
- `language`: language of the file 
- `owner_user_id`: ID of the user who owns the asset 
- `asset`: the actual asset file 
- `app_id`: ID of the application 
- `feature_id`: ID of the feature associated with the asset

The `assets` table is linked with `feature` and `app` tables to maintain consistency and enable easy access to related data.

For consistent management, assets are organised in folders:

`../assets/app_name/feature_name/language`

Additionally, folder `i18n` store translation-related files such as `assets.pot` and `assets.po`. And `scripts` stores automation scripts themselves.

### Automation Scripts 

##### Preparation

Scripts are designed to be performed on local machine. First, make sure you have the connection to `user-profile-api` and you have working links to the database. Links should be placed in `config.py` - see `config_template.py` for the instructions. Second, scripts use `sqlalchemy` to connect with database. Make sure you have `sqlalchemy` for Python installed. 

##### add_new_assets.py

- Purpose: To add new asset files from GitHub to the database and update existing ones. 
- Usage:
  - Place the asset file in the appropriate GitHub folder or update the file needed. 
  - Run the script to automatically add the file to the `assets` table in the database.
  - When new asset is added, fill in the description. 

##### export_assets.py

- Purpose: To export assets from the database to the GitHub folder. 
- Usage:
  - Run the script to ensure all assets in the database are mirrored in the GitHub repository.

##### export_markdown_to_pot.py

- Purpose: To convert existing Markdown files of database to POT format for translation.
- Usage:
  - Run the script to extract strings from Markdown files and export them as POT file.

##### import_po_to_db.py

- Purpose: To import translated PO files into the database.
- Usage:
  - Run the script to add translated PO files to the database as Markdown files, enabling multilingual support.
  - Make sure all new strings have the right description.