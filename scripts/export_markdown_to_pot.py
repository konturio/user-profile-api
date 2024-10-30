import os
import hashlib

def get_english_markdown_assets(assets_folder):
    assets = []
    for dirpath, _, filenames in os.walk(assets_folder):
        # Ensure we're only looking in "en" folders
        if 'en' not in os.path.normpath(dirpath).split(os.sep):
            continue

        for filename in filenames:
            if filename.endswith('.md'):
                file_path = os.path.join(dirpath, filename)
                with open(file_path, 'r', encoding='utf-8') as file:
                    content = file.read()
                assets.append((file_path, content))
    return assets

def export_to_pot_file(translatable_entries, output_file, assets_folder):
    os.makedirs(os.path.dirname(output_file), exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('msgid ""\n')
        f.write('msgstr ""\n')
        f.write('"Content-Type: text/plain; charset=UTF-8\\n"\n')
        f.write('"Language: en\\n"\n\n')

        for file_paths, string in translatable_entries.values():
            # Write each file path as a comment
            for path in file_paths:
                relative_path = os.path.relpath(path, os.path.dirname(assets_folder))
                f.write(f'#: {relative_path}\n')
            escaped_string = string.replace('\n', '\\n').replace('"', '\\"')
            f.write(f'msgid "{escaped_string}"\n')
            f.write('msgstr ""\n\n')

def export_markdown_assets_to_gettext(assets_folder, output_file):
    assets = get_english_markdown_assets(assets_folder)
    translatable_entries = {}

    for file_path, content in assets:
        # Create a unique hash for each content string to identify duplicates
        content_hash = hashlib.md5(content.encode('utf-8')).hexdigest()

        # If the content hash is new, add it; otherwise, append the file path
        if content_hash not in translatable_entries:
            translatable_entries[content_hash] = ([file_path], content)
        else:
            translatable_entries[content_hash][0].append(file_path)

    export_to_pot_file(translatable_entries, output_file, assets_folder)

# Define the assets folder and output file path
project_root = os.path.dirname(os.path.dirname(__file__))
assets_folder = os.path.join(project_root, 'assets')
output_file = os.path.join(project_root, 'i18n/template/assets.pot')

# Run the export
export_markdown_assets_to_gettext(assets_folder, output_file)