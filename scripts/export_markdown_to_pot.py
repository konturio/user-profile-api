from models import get_english_markdown_assets


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
    translatable_strings = [asset.asset.decode('utf-8') for asset in assets]

    export_to_pot_file(translatable_strings, output_file)

# Specify the output file and run the export
output_file = 'i18n/template/assets.pot'
export_markdown_assets_to_gettext(output_file)
