--liquibase formatted sql

--changeset user-profile-service:18216-add-dn2-assets-into-ups.sql runOnChange:true

--insert DN icons
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('image',
            'svg',
            'favicon.svg',
            'Disaster Ninja favicon in svg format',
            'null',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            ''::bytea);
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('image',
            'ico',
            'favicon.ico',
            'Disaster Ninja favicon in ico format',
            'null',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            ''::bytea);
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('image',
            'png',
            'apple-touch-icon.png',
            'Disaster Ninja Apple touch icon',
            'null',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            ''::bytea);
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('image',
            'png',
            'icon-192x192.png',
            'Disaster Ninja 192x192px icon',
            'null',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            ''::bytea);
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('image',
            'png',
            'icon-512x512.png',
            'Disaster Ninja 512x512px icon',
            'null',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            ''::bytea);

--insert DN About page in Arabic
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Arabic',
            'ar',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Ù…Ø±Ø­Ø¨Ù‹Ø§ ÙÙŠ Disaster Ninja!
=========================

Ù‡Ù„ ØªØ±ÙŠØ¯ Ø£Ù† ÙŠØªÙ… Ø¥Ø®Ø·Ø§Ø±Ùƒ Ø¨Ø§Ù„ÙƒÙˆØ§Ø±Ø« Ø§Ù„Ø¬Ø§Ø±ÙŠØ©ØŸ Ù‡Ù„ Ø£Ù†Øª Ù…Ù‡ØªÙ… Ø¨Ø§Ù„Ø¨ÙŠØ§Ù†Ø§Øª Ø§Ù„Ø³ÙƒØ§Ù†ÙŠØ© Ø§Ù„ÙÙˆØ±ÙŠØ© ÙˆØ§Ù„ØªØ­Ù„ÙŠÙ„Ø§Øª Ø§Ù„Ø£Ø®Ø±Ù‰ Ù„Ø£ÙŠ Ù…Ù†Ø·Ù‚Ø© ÙÙŠ Ø§Ù„Ø¹Ø§Ù„Ù…ØŸ ÙŠØ¹Ø±Ø¶ Disaster Ninja Ø¨Ø¹Ø¶ Ù‚Ø¯Ø±Ø§Øª [Kontur](https://www.kontur.io/) ÙÙŠ ØªÙ„Ø¨ÙŠØ© Ù‡Ø°Ù‡ Ø§Ù„Ø§Ø­ØªÙŠØ§Ø¬Ø§Øª.

Ù„Ù‚Ø¯ ØµÙ…Ù…Ù†Ø§Ù‡ ÙÙŠ Ø§Ù„Ø¨Ø¯Ø§ÙŠØ© Ù„ÙŠÙƒÙˆÙ† Ø£Ø¯Ø§Ø© Ø¯Ø¹Ù… Ù‚Ø±Ø§Ø± Ù„Ù…ØµÙ…Ù…ÙŠ Ø§Ù„Ø®Ø±Ø§Ø¦Ø· Ø§Ù„Ø¥Ù†Ø³Ø§Ù†ÙŠØ©. Ø§Ù„Ø¢Ù†ØŒ Ø§Ø²Ø¯Ø§Ø¯Øª ÙˆØ¸Ø§Ø¦ÙÙ‡ ÙˆØ­Ø§Ù„Ø§Øª Ø§Ø³ØªØ®Ø¯Ø§Ù…Ù‡. Ø³ÙˆØ§Ø¡ ÙƒÙ†Øª ØªØ¹Ù…Ù„ ÙÙŠ Ø¥Ø¯Ø§Ø±Ø© Ø§Ù„ÙƒÙˆØ§Ø±Ø«ØŒ Ø£Ùˆ ØªØ¨Ù†ÙŠ Ù…Ø¯ÙŠÙ†Ø© Ø°ÙƒÙŠØ©ØŒ Ø£Ùˆ ØªØ¬Ø±ÙŠ Ø¨Ø­Ø«Ù‹Ø§ Ø¹Ù† ØªØºÙŠØ± Ø§Ù„Ù…Ù†Ø§Ø®ØŒ ÙŠÙ…ÙƒÙ† Ø£Ù† ÙŠØ³Ø§Ø¹Ø¯Ùƒ Disaster Ninja Ø¹Ù„Ù‰:

> ### 1\. Ø§Ù„Ø¨Ù‚Ø§Ø¡ Ø¹Ù„Ù‰ Ø§Ø·Ù„Ø§Ø¹ Ø¨Ø¢Ø®Ø± Ø§Ù„ØªØ·ÙˆØ±Ø§Øª Ù„Ù„Ø£Ø­Ø¯Ø§Ø« Ø§Ù„Ø®Ø·Ø±Ø© Ø¹Ù„Ù‰ Ù…Ø³ØªÙˆÙ‰ Ø§Ù„Ø¹Ø§Ù„Ù….
>
> ÙŠØªÙ… ØªØ­Ø¯ÙŠØ« Ù„ÙˆØ­Ø© Ø§Ù„ÙƒÙˆØ§Ø±Ø« Ø¨Ø§Ø³ØªÙ…Ø±Ø§Ø± Ù„Ø¥Ø¨Ù„Ø§ØºÙƒ Ø¨Ø§Ù„Ø£Ø­Ø¯Ø§Ø« Ø§Ù„Ø¬Ø§Ø±ÙŠØ©. Ø¥Ù†Ù‡Ø§ ØªØ³ØªÙ‡Ù„Ùƒ Ø§Ù„Ø¨ÙŠØ§Ù†Ø§Øª Ù…Ù† [Kontur Event Feed](https://www.kontur.io/portfolio/event-feed/) ÙˆØ§Ù„ØªÙŠ ÙŠÙ…ÙƒÙ†Ùƒ Ø£ÙŠØ¶Ù‹Ø§ Ø§Ù„ÙˆØµÙˆÙ„ Ø¥Ù„ÙŠÙ‡Ø§ Ø¹Ø¨Ø± ÙˆØ§Ø¬Ù‡Ø© Ø¨Ø±Ù…Ø¬Ø© Ø§Ù„ØªØ·Ø¨ÙŠÙ‚Ø§Øª.
>
> ### 2\. Ø§Ù„ØªØ±ÙƒÙŠØ² Ø¹Ù„Ù‰ Ù…Ø¬Ø§Ù„ Ø§Ù‡ØªÙ…Ø§Ù…Ùƒ.
>
> ØªØªÙŠØ­ Ù„Ùƒ Ù„ÙˆØ­Ø© Ø£Ø¯ÙˆØ§Øª Ø§Ù„Ø±Ø³Ù… Ø±Ø³Ù… Ø£Ùˆ ØªØ­Ù…ÙŠÙ„ Ø§Ù„Ø´ÙƒÙ„ Ø§Ù„Ù‡Ù†Ø¯Ø³ÙŠ Ø§Ù„Ø®Ø§Øµ Ø¨Ùƒ Ø¹Ù„Ù‰ Ø§Ù„Ø®Ø±ÙŠØ·Ø©. ÙŠÙ…ÙƒÙ†Ùƒ Ø£ÙŠØ¶Ù‹Ø§ Ø§Ù„ØªØ±ÙƒÙŠØ² Ø¹Ù„Ù‰ Ù…Ù†Ø·Ù‚Ø© Ù…Ø¹Ø±Ø¶Ø© Ù„Ù„ÙƒÙˆØ§Ø±Ø« Ø£Ùˆ ÙˆØ­Ø¯Ø© Ø¥Ø¯Ø§Ø±ÙŠØ© - Ø¨Ù„Ø¯ Ø£Ùˆ Ù…Ø¯ÙŠÙ†Ø© Ø£Ùˆ Ù…Ù†Ø·Ù‚Ø©.
>
> ### 3\. Ø§Ù„Ø­ØµÙˆÙ„ Ø¹Ù„Ù‰ ØªØ­Ù„ÙŠÙ„Ø§Øª Ù„Ù…Ù†Ø·Ù‚Ø© Ø§Ù„Ø§Ù‡ØªÙ…Ø§Ù….
>
> ØªØ¹Ø±Ø¶ Ù„ÙˆØ­Ø© Ø§Ù„ØªØ­Ù„ÙŠÙ„Ø§Øª Ø¹Ø¯Ø¯ Ø§Ù„Ø£Ø´Ø®Ø§Øµ Ø§Ù„Ø°ÙŠÙ† ÙŠØ¹ÙŠØ´ÙˆÙ† ÙÙŠ ØªÙ„Ùƒ Ø§Ù„Ù…Ù†Ø·Ù‚Ø© Ø­Ø³Ø¨ [Kontur Population](https://data.humdata.org/dataset/kontur-population-dataset) ÙˆÙØ¬ÙˆØ§Øª Ø§Ù„Ø®Ø±Ø§Ø¦Ø· Ø§Ù„Ù…Ù‚Ø¯Ø±Ø© ÙÙŠ OpenStreetMap. ÙŠÙ…ÙƒÙ† Ù„Ø¹Ù…Ù„Ø§Ø¡ Kontur Ø§Ù„ÙˆØµÙˆÙ„ Ø¥Ù„Ù‰ Ù…Ø¦Ø§Øª Ø§Ù„Ù…Ø¤Ø´Ø±Ø§Øª Ø§Ù„Ø£Ø®Ø±Ù‰ Ù…Ù† Ø®Ù„Ø§Ù„ Ø§Ù„ØªØ­Ù„ÙŠÙ„Ø§Øª Ø§Ù„Ù…ØªÙ‚Ø¯Ù…Ø©.
>
> ### 4\. Ø§Ø³ØªÙƒØ´Ø§Ù Ø§Ù„Ø¨ÙŠØ§Ù†Ø§Øª Ø¹Ù„Ù‰ Ø§Ù„Ø®Ø±ÙŠØ·Ø© ÙˆØ§Ù„ØªÙˆØµÙ„ Ø¥Ù„Ù‰ Ø§Ø³ØªÙ†ØªØ§Ø¬Ø§Øª.
>
> ØªÙ…Ù†Ø­Ùƒ Ù„ÙˆØ­Ø© Layers Ø®ÙŠØ§Ø±Ø§Øª Ù…ØªÙ†ÙˆØ¹Ø© Ù„Ø¹Ø±Ø¶ Ù…Ø¤Ø´Ø±ÙŠÙ† ÙÙŠ ÙˆÙ‚Øª ÙˆØ§Ø­Ø¯ Ø¹Ù„Ù‰ Ø®Ø±ÙŠØ·Ø© Ø«Ù†Ø§Ø¦ÙŠØ© Ø§Ù„Ù…ØªØºÙŠØ± Ø¹Ù„Ù‰ Ø³Ø¨ÙŠÙ„ Ø§Ù„Ù…Ø«Ø§Ù„ Ø§Ù„ÙƒØ«Ø§ÙØ© Ø§Ù„Ø³ÙƒØ§Ù†ÙŠØ© ÙˆØ§Ù„Ù…Ø³Ø§ÙØ© Ø¥Ù„Ù‰ Ø£Ù‚Ø±Ø¨ Ù…Ø­Ø·Ø© Ø¥Ø·ÙØ§Ø¡. Ø§Ø³ØªØ®Ø¯Ù… ÙˆØ³ÙŠÙ„Ø© Ø¥ÙŠØ¶Ø§Ø­ Ø§Ù„Ù„ÙˆÙ† Ù„ØªÙ‚ÙŠÙŠÙ… Ø§Ù„Ù…Ù†Ø§Ø·Ù‚ Ø§Ù„ØªÙŠ ØªØªØ·Ù„Ø¨ Ø§Ù„Ø§Ù†ØªØ¨Ø§Ù‡.
> Ø¨Ø´ÙƒÙ„ Ø¹Ø§Ù…ØŒ ÙŠØ´ÙŠØ± Ø§Ù„Ù„ÙˆÙ† Ø§Ù„Ø£Ø®Ø¶Ø± Ø¥Ù„Ù‰ Ù…Ø®Ø§Ø·Ø± Ù…Ù†Ø®ÙØ¶Ø© / ÙØ¬ÙˆØ§Øª Ù‚Ù„ÙŠÙ„Ø©ØŒ ÙˆØ§Ù„Ø£Ø­Ù…Ø± - Ù…Ø®Ø§Ø·Ø± Ø¹Ø§Ù„ÙŠØ© / Ø§Ù„Ø¹Ø¯ÙŠØ¯ Ù…Ù† Ø§Ù„ÙØ¬ÙˆØ§Øª.

Ø¨Ø§Ù„Ø¥Ø¶Ø§ÙØ© Ø¥Ù„Ù‰ Ø°Ù„Ùƒ ØŒ ÙŠÙ…ÙƒÙ†Ùƒ Ø§Ù„ØªØ¨Ø¯ÙŠÙ„ Ø¥Ù„Ù‰ Ø§Ù„ØªÙ‚Ø§Ø±ÙŠØ± ÙÙŠ Ø§Ù„Ù„ÙˆØ­Ø© Ø§Ù„ÙŠÙ…Ù†Ù‰ Ù„Ù„ÙˆØµÙˆÙ„ Ø¥Ù„Ù‰ Ø§Ù„Ø¨ÙŠØ§Ù†Ø§Øª Ø§Ù„Ù…ØªØ¹Ù„Ù‚Ø© Ø¨Ø§Ù„Ø£Ø®Ø·Ø§Ø¡ ÙˆØ§Ù„ØªÙ†Ø§Ù‚Ø¶Ø§Øª Ø§Ù„Ù…Ø­ØªÙ…Ù„Ø© ÙÙŠ OpenStreetMap ÙˆØ§Ù„Ù…Ø³Ø§Ø¹Ø¯Ø© ÙÙŠ Ø¥ØµÙ„Ø§Ø­Ù‡Ø§ Ø¹Ù† Ø·Ø±ÙŠÙ‚ Ø±Ø³Ù… Ø®Ø±ÙŠØ·Ø© Ø§Ù„Ù…Ù†Ø·Ù‚Ø© Ø§Ù„Ù…Ø¹Ù†ÙŠØ© Ø¨Ø§Ø³ØªØ®Ø¯Ø§Ù… Ù…Ø­Ø±Ø± JOSM.

### [Ø§Ù„Ø§Ù†ØªÙ‚Ø§Ù„ Ø¥Ù„Ù‰ Ø§Ù„Ø®Ø±ÙŠØ·Ø© Ø§Ù„Ø¢Ù† âžœ](/ "map")

Ù†Ø£Ù…Ù„ Ø£Ù† ØªØ¬Ø¯ Ù‡Ø°Ù‡ Ø§Ù„Ø£Ø¯Ø§Ø© Ø°Ø§Øª Ù‚ÙŠÙ…Ø©. Ø§Ø³ØªØ®Ø¯Ù… Ù…Ø±Ø¨Ø¹ Ø§Ù„Ø¯Ø±Ø¯Ø´Ø© Ø¹Ù„Ù‰ Disaster Ninja Ù„Ø£ÙŠØ© Ø£Ø³Ø¦Ù„Ø© Ø­ÙˆÙ„ ÙˆØ¸Ø§Ø¦ÙÙ‡ØŒ ÙˆØ³Ù†ÙƒÙˆÙ† Ø³Ø¹Ø¯Ø§Ø¡ Ø¨Ø¥Ø±Ø´Ø§Ø¯Ùƒ. ÙŠÙ…ÙƒÙ†Ùƒ Ø£ÙŠØ¶Ù‹Ø§ Ø§Ù„ØªÙˆØ§ØµÙ„ Ù…Ø¹Ù†Ø§ Ø¹Ø¨Ø± Ø§Ù„Ø¨Ø±ÙŠØ¯ Ø§Ù„Ø¥Ù„ÙƒØªØ±ÙˆÙ†ÙŠ[hello@kontur.io](mailto:hello@kontur.io) Ø¥Ø°Ø§ ÙƒØ§Ù† Ù„Ø¯ÙŠÙƒ Ù…Ù„Ø§Ø­Ø¸Ø§Øª Ø£Ùˆ Ø§Ù‚ØªØ±Ø§Ø­Ø§Øª Ø¨Ø´Ø£Ù† ØªØ­Ø³ÙŠÙ† Ø§Ù„Ø£Ø¯Ø§Ø©.

ÙŠÙØ¹Ø¯Ù‘ Disaster Ninja Ù…Ø´Ø±ÙˆØ¹Ù‹Ø§ Ù…ÙØªÙˆØ­ Ø§Ù„Ù…ØµØ¯Ø±. ØªØ­Ù‚Ù‚ Ù…Ù† Ø§Ù„ÙƒÙˆØ¯ ÙÙŠ Ø­Ø³Ø§Ø¨ [Kontur](https://github.com/konturio) Ø¹Ù„Ù‰ [GitHub](https://github.com/konturio).'::bytea);

--insert DN About page in Indonesian
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Indonesian',
            'id',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Selamat datang di Disaster Ninja!
=================================

Apakah Anda ingin menerima kabar tentang bencana yang sedang terjadi? Anda tertarik dengan data populasi dan analitik lainnya yang tersaji seketika untuk wilayah mana pun di dunia? Disaster Ninja menampilkan beberapa kemampuan dari [Kontur](https://www.kontur.io/) dalam mengatasi berbagai kebutuhan ini.

Pada awalnya, kami merancang alat ini sebagai alat bantu untuk pemetaan kemanusiaan. Kini, fungsionalitas dan kasus penggunaannya telah bertambah. Apakah Anda bekerja pada pengelolaan bencana, membangun kota cerdas, ataupun meneliti perubahan iklim, Disaster Ninja dapat membantu Anda untuk:

> ### 1\. Selalu mendapatkan kabar terbaru tentang kejadian berbahaya terkini di seluruh dunia.
>
> Panel Bencana disegarkan secara terus-menerus untuk memberi tahu Anda kejadian yang sedang berlangsung. Panel ini memakai data dari [Feed Kejadian Kontur](https://www.kontur.io/portfolio/event-feed/), yang juga dapat diakses melalui API.
>
> ### 2\. Fokus pada area perhatian Anda.
>
> Panel Peralatan Gambar memungkinkan Anda menggambar atau mengunggah geometri Anda sendiri pada peta. Anda juga dapat berfokus pada area yang terpapar bencana atau satuan administratif â€” negara, kota, atau wilayah.
>
> ### 3\. Dapatkan analitik untuk area yang menjadi fokus.
>
> Panel Analitik memperlihatkan jumlah orang yang tinggal di area tersebut untuk setiap [Populasi Kontur](https://data.humdata.org/dataset/kontur-population-dataset) dan perkiraan kesenjangan pemetaan di OpenStreetMap. Pelanggan Kontur memiliki akses ke ratusan indikator lainnya melalui Analitik Lanjut.
>
> ### 4\. Selidiki data pada peta dan buat kesimpulan.
>
> Panel Lapisan memberi Anda berbagai opsi untuk menampilkan dua indikator secara serentak pada peta bivariat, misalnya kepadatan populasi dan jarak ke stasiun pemadam kebakaran terdekat. Gunakan legenda warna untuk menilai area mana yang perlu diperhatikan.
> Petunjuk: secara umum, warna hijau menunjukkan risiko rendah/sedikit kesenjangan, warna merah â€” risiko tinggi/banyak kesenjangan.

Selain itu, Anda dapat beralih ke Laporan di panel kiri untuk mengakses data tentang potensi kesalahan dan inkonsistensi di OpenStreetMap serta membantu memperbaikinya dengan memetakan masing-masing area menggunakan editor JOSM.

### [Buka peta sekarang âžœ](/ "map")

Kami berharap agar alat ini bermanfaat bagi Anda. Gunakan kotak obrolan di Disaster Ninja untuk setiap pertanyaan tentang fungsionalitas, dan kami dengan senang hati akan memandu Anda. Anda juga dapat menghubungi kami melalui email di [hello@kontur.io](mailto:hello@kontur.io) jika Anda memiliki tanggapan atau saran untuk meningkatkan alat ini.

Disaster Ninja adalah proyek sumber terbuka. Temukan kodenya di [akun GitHub Kontur](https://github.com/konturio).'::bytea);

--insert DN About page in Korean
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Korean',
            'ko',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Disaster Ninjaì— ì˜¤ì‹  ê²ƒì„ í™˜ì˜í•©ë‹ˆë‹¤!
============================

í˜„ìž¬ ì§„í–‰ ì¤‘ì¸ ìž¬ë‚œì— ëŒ€í•´ ì•Œë¦¼ì„ ë°›ì•„ë³´ì‹œê² ìŠµë‹ˆê¹Œ? ì „ ì„¸ê³„ ëª¨ë“  ì§€ì—­ì˜ ì¸êµ¬ ë°ì´í„°ì™€ ê¸°íƒ€ ë¶„ì„ ì‚¬í•­ì„ ì¦‰ì‹œ ì•Œì•„ë³´ê³  ì‹¶ìœ¼ì‹ ê°€ìš”? Disaster Ninjaì—ì„œëŠ” ì´ëŸ¬í•œ ìš”êµ¬ ì‚¬í•­ì„ í•´ê²°í•˜ê¸° ìœ„í•´ ëª‡ ê°€ì§€ [Kontur](https://www.kontur.io/) ê¸°ëŠ¥ì„ ì‚¬ìš©í•©ë‹ˆë‹¤.

ì²˜ìŒì—ëŠ” ì¸ë„ì£¼ì˜ì ì¸ ë¬¸ì œë¥¼ ë‹¤ë£¨ê¸° ìœ„í•œ ì˜ì‚¬ ê²°ì • ì§€ì› ë„êµ¬ë¡œ ê³ ì•ˆë˜ì—ˆì§€ë§Œ, ì´ì œëŠ” ê¸°ëŠ¥ê³¼ ìš©ë²•ì´ ì—¬ëŸ¬ ê°€ì§€ë¡œ ëŠ˜ì–´ë‚¬ìŠµë‹ˆë‹¤. ìž¬ë‚œ ê´€ë¦¬, ìŠ¤ë§ˆíŠ¸ ì‹œí‹° êµ¬ì¶•, ê¸°í›„ ë³€í™”ì— ê´€í•œ ì—°êµ¬ ìˆ˜í–‰ ë“± ì–´ë–¤ ì—…ë¬´ë¥¼ í•˜ë”ë¼ë„ ë‹¤ìŒê³¼ ê°™ì€ ë„ì›€ì„ ë“œë¦´ ìˆ˜ ìžˆìŠµë‹ˆë‹¤.

> ### 1\. ì „ ì„¸ê³„ì˜ ìµœì‹  ìœ„í—˜ ì´ë²¤íŠ¸ì˜ ë™í–¥ì„ ì•Œë ¤ ë“œë¦½ë‹ˆë‹¤.
>
> ìž¬ë‚œ íŒ¨ë„ì´ ì§€ì†ì ìœ¼ë¡œ ìƒˆë¡œ ê³ ì¹¨ ë˜ì–´ í˜„ìž¬ ì¼ì–´ë‚˜ëŠ” ì´ë²¤íŠ¸ì— ëŒ€í•œ ì •ë³´ë¥¼ ì œê³µí•©ë‹ˆë‹¤. [Kontur ì´ë²¤íŠ¸ í”¼ë“œ](https://www.kontur.io/portfolio/event-feed/)ì˜ ë°ì´í„°ë¥¼ ì‚¬ìš©í•˜ë©°, APIë¥¼ í†µí•´ì„œë„ í•´ë‹¹ ë°ì´í„°ì— ì•¡ì„¸ìŠ¤í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.
>
> ### 2\. ê´€ì‹¬ ì˜ì—­ì„ ì§‘ì¤‘ì ìœ¼ë¡œ ë³´ì—¬ ë“œë¦½ë‹ˆë‹¤.
>
> ê·¸ë¦¬ê¸° ë„êµ¬ íŒ¨ë„ì„ ì‚¬ìš©í•˜ë©´ ìžì²´ ê¸°í•˜ ë„í˜•ì„ ì§€ë„ì— ê·¸ë¦¬ê±°ë‚˜ ì—…ë¡œë“œí•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤. ìž¬ë‚œì— ë…¸ì¶œëœ ì˜ì—­ì´ë‚˜ í–‰ì • ë‹¨ìœ„(ì˜ˆ: êµ­ê°€, ë„ì‹œ, ì§€ì—­)ì— ì´ˆì ì„ ë§žì¶œ ìˆ˜ë„ ìžˆìŠµë‹ˆë‹¤.
>
> ### 3\. ì§‘ì¤‘ ì˜ì—­ì— ëŒ€í•œ ë¶„ì„ì„ ì œê³µí•©ë‹ˆë‹¤.
>
> ë¶„ì„ íŒ¨ë„ì€ [Kontur ì¸êµ¬](https://data.humdata.org/dataset/kontur-population-dataset)ë‹¹ í•´ë‹¹ ì˜ì—­ì— ê±°ì£¼í•˜ëŠ” ì‚¬ëžŒì˜ ìˆ˜ì™€ OpenStreetMap ë‚´ ì˜ˆìƒ ë§¤í•‘ ê°­ì„ ë³´ì—¬ì¤ë‹ˆë‹¤. ê·¸ ì™¸ì—ë„ Kontur ê³ ê°ì€ ê³ ê¸‰ ë¶„ì„ì„ í†µí•´ ìˆ˜ë°± ê°€ì§€ ì§€í‘œì— ì•¡ì„¸ìŠ¤í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.
>
> ### 4\. ì§€ë„ì˜ ë°ì´í„°ë¥¼ íƒìƒ‰í•˜ì—¬ ê²°ë¡ ì„ ë‚´ë¦´ ìˆ˜ ìžˆìŠµë‹ˆë‹¤.
>
> ë ˆì´ì–´ íŒ¨ë„ì„ ì‚¬ìš©í•˜ë©´ ì´ë³€ìˆ˜ ì§€ë„ì—ì„œ ë‘ ê°€ì§€ ì§€í‘œë¥¼ ë™ì‹œì— í‘œì‹œí•˜ëŠ” ë‹¤ì–‘í•œ ì˜µì…˜ì„ ì‚¬ìš©í•  ìˆ˜ ìžˆê²Œ ë©ë‹ˆë‹¤(ì˜ˆ: ì¸êµ¬ ë°€ë„, ê°€ìž¥ ê°€ê¹Œìš´ ì†Œë°©ì„œê¹Œì§€ì˜ ê±°ë¦¬). ìƒ‰ìƒ ë²”ë¡€ë¥¼ ì‚¬ìš©í•˜ì—¬ ì£¼ì˜ê°€ í•„ìš”í•œ ì˜ì—­ì„ í‰ê°€í•˜ì„¸ìš”.
> ížŒíŠ¸: ì¼ë°˜ì ìœ¼ë¡œ ë…¹ìƒ‰ì€ ì €ìœ„í—˜/ì ì€ ê°­ì„ ì˜ë¯¸í•˜ê³  ë¹¨ê°„ìƒ‰ì€ ê³ ìœ„í—˜/ë§Žì€ ê°­ì„ ì˜ë¯¸í•©ë‹ˆë‹¤.

ë˜í•œ, ì™¼ìª½ íŒ¨ë„ì˜ ë³´ê³ ì„œë¡œ ì´ë™í•˜ì—¬ OpenStreetMapì˜ ìž ìž¬ì ì¸ ì˜¤ë¥˜ ë° ë¶ˆì¼ì¹˜ ë°ì´í„°ì— ì•¡ì„¸ìŠ¤í•˜ê³ , JOSM íŽ¸ì§‘ê¸°ë¥¼ í†µí•´ ê° ì˜ì—­ì„ ë§¤í•‘í•˜ì—¬ í•´ë‹¹ ë°ì´í„°ë¥¼ ìˆ˜ì •í•  ìˆ˜ë„ ìžˆìŠµë‹ˆë‹¤.

### [ì§€ê¸ˆ ë°”ë¡œ ì§€ë„ë¡œ ì´ë™í•˜ì„¸ìš”. âžœ](/ "map")

ì´ ë„êµ¬ê°€ ë§Žì€ ë„ì›€ì´ ë˜ê¸°ë¥¼ ë°”ëžë‹ˆë‹¤. ê¸°ëŠ¥ì— ëŒ€í•´ ê¶ê¸ˆí•œ ì ì€ Disaster Ninjaì˜ ì±—ë°•ìŠ¤ë¥¼ í†µí•´ ì–¸ì œë“ ì§€ ë¬¸ì˜í•´ ì£¼ì‹œë©´ ë„ì™€ ë“œë¦¬ê² ìŠµë‹ˆë‹¤. ë„êµ¬ ê°œì„ ì— ëŒ€í•œ í”¼ë“œë°±ì´ë‚˜ ì œì•ˆ ì‚¬í•­ì´ ìžˆì„ ê²½ìš°, [hello@kontur.io](mailto:hello@kontur.io) ë¡œ ì´ë©”ì¼ ì£¼ì…”ë„ ë©ë‹ˆë‹¤.

Disaster NinjaëŠ” ì˜¤í”ˆ ì†ŒìŠ¤ í”„ë¡œì íŠ¸ìž…ë‹ˆë‹¤. [Konturì˜ GitHub ê³„ì •](https://github.com/konturio)ì—ì„œ ì½”ë“œë¥¼ ì°¾ì•„ë³´ì„¸ìš”.'::bytea);

--insert DN About page in Ukrainian
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Ukrainian',
            'uk',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Ð’Ñ–Ñ‚Ð°Ñ”Ð¼Ð¾ Ð½Ð° Disaster Ninja!
==========================

Ð’Ð¸ Ñ…Ð¾Ñ‡ÐµÑ‚Ðµ Ð¾Ñ‚Ñ€Ð¸Ð¼ÑƒÐ²Ð°Ñ‚Ð¸ Ð¿Ð¾Ð²Ñ–Ð´Ð¾Ð¼Ð»ÐµÐ½Ð½Ñ Ð¿Ñ€Ð¾ ÐºÐ°Ñ‚Ð°ÑÑ‚Ñ€Ð¾Ñ„Ð¸? Ð’Ð°Ñ Ñ†Ñ–ÐºÐ°Ð²Ð»ÑÑ‚ÑŒ Ð¼Ð¸Ñ‚Ñ‚Ñ”Ð²Ñ– Ð´Ð°Ð½Ñ– Ð¿Ñ€Ð¾ Ð½Ð°ÑÐµÐ»ÐµÐ½Ð½Ñ Ñ‚Ð° Ñ–Ð½ÑˆÐ° Ð°Ð½Ð°Ð»Ñ–Ñ‚Ð¸ÐºÐ° Ð´Ð»Ñ Ð±ÑƒÐ´ÑŒ-ÑÐºÐ¾Ð³Ð¾ Ñ€ÐµÐ³Ñ–Ð¾Ð½Ñƒ ÑÐ²Ñ–Ñ‚Ñƒ? Disaster Ninja Ð²Ñ–Ð´ [Kontur](https://www.kontur.io/) Ð¼Ð¾Ð¶Ðµ Ð·Ð°Ð´Ð¾Ð²Ð¾Ð»ÑŒÐ½Ð¸Ñ‚Ð¸ Ñ†Ñ– Ð¿Ð¾Ñ‚Ñ€ÐµÐ±Ð¸.

ÐœÐ¸ Ñ€Ð¾Ð·Ñ€Ð¾Ð±Ð¸Ð»Ð¸ Ñ†Ðµ ÑÐº Ñ–Ð½ÑÑ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚ Ð¿Ñ–Ð´Ñ‚Ñ€Ð¸Ð¼ÐºÐ¸ Ð¿Ñ€Ð¸Ð¹Ð½ÑÑ‚Ñ‚Ñ Ñ€Ñ–ÑˆÐµÐ½ÑŒ Ð´Ð»Ñ Ð³ÑƒÐ¼Ð°Ð½Ñ–Ñ‚Ð°Ñ€Ð½Ð¸Ñ… ÐºÐ°Ñ€Ñ‚Ð¾Ð³Ñ€Ð°Ñ„Ñ–Ð². Ð¢ÐµÐ¿ÐµÑ€ Ð¹Ð¾Ð³Ð¾ Ñ„ÑƒÐ½ÐºÑ†Ñ–Ð¾Ð½Ð°Ð»ÑŒÐ½Ñ–ÑÑ‚ÑŒ Ð·Ñ€Ð¾ÑÐ»Ð°. ÐÐµÐ·Ð°Ð»ÐµÐ¶Ð½Ð¾ Ð²Ñ–Ð´ Ñ‚Ð¾Ð³Ð¾, Ñ‡Ð¸ Ð¿Ñ€Ð°Ñ†ÑŽÑ”Ñ‚Ðµ Ð²Ð¸ Ð² ÑÑ„ÐµÑ€Ñ– Ð±Ð¾Ñ€Ð¾Ñ‚ÑŒÐ±Ð¸ Ð·Ñ– ÑÑ‚Ð¸Ñ…Ñ–Ð¹Ð½Ð¸Ð¼Ð¸ Ð»Ð¸Ñ…Ð°Ð¼Ð¸, Ð±ÑƒÐ´ÑƒÑ”Ñ‚Ðµ Ñ€Ð¾Ð·ÑƒÐ¼Ð½Ðµ Ð¼Ñ–ÑÑ‚Ð¾ Ñ‡Ð¸ Ð¿Ñ€Ð¾Ð²Ð¾Ð´Ð¸Ñ‚Ðµ Ð´Ð¾ÑÐ»Ñ–Ð´Ð¶ÐµÐ½Ð½Ñ Ñ‰Ð¾Ð´Ð¾ Ð·Ð¼Ñ–Ð½Ð¸ ÐºÐ»Ñ–Ð¼Ð°Ñ‚Ñƒ, Disaster Ninja Ð¼Ð¾Ð¶Ðµ Ð²Ð°Ð¼ Ð´Ð¾Ð¿Ð¾Ð¼Ð¾Ð³Ñ‚Ð¸:

> ### 1\. Ð‘ÑƒÐ´ÑŒÑ‚Ðµ Ð² ÐºÑƒÑ€ÑÑ– Ð¾ÑÑ‚Ð°Ð½Ð½Ñ–Ñ… ÐºÐ°Ñ‚Ð°ÑÑ‚Ñ€Ð¾Ñ„ Ñƒ Ð²ÑÑŒÐ¾Ð¼Ñƒ ÑÐ²Ñ–Ñ‚Ñ–.
>
> ÐŸÐ°Ð½ÐµÐ»ÑŒ ÐšÐ°Ñ‚Ð°ÑÑ‚Ñ€Ð¾Ñ„ Ð¿Ð¾ÑÑ‚Ñ–Ð¹Ð½Ð¾ Ð¾Ð½Ð¾Ð²Ð»ÑŽÑ”Ñ‚ÑŒÑÑ, Ñ‰Ð¾Ð± Ñ–Ð½Ñ„Ð¾Ñ€Ð¼ÑƒÐ²Ð°Ñ‚Ð¸ Ð²Ð°Ñ Ð¿Ñ€Ð¾ Ð¿Ð¾Ñ‚Ð¾Ñ‡Ð½Ñ– Ð¿Ð¾Ð´Ñ–Ñ—. Ð”Ð°Ð½Ñ– Ð¾Ñ‚Ñ€Ð¸Ð¼Ð°Ð½Ñ– Ð· ÐºÐ°Ð½Ð°Ð»Ñƒ Ð¿Ð¾Ð´Ñ–Ð¹ [Kontur](https://www.kontur.io/portfolio/event-feed/), Ð´Ð¾ ÑÐºÐ¾Ð³Ð¾ Ñ‚Ð°ÐºÐ¾Ð¶ Ð¼Ð¾Ð¶Ð½Ð° Ð¾Ñ‚Ñ€Ð¸Ð¼Ð°Ñ‚Ð¸ Ð´Ð¾ÑÑ‚ÑƒÐ¿ Ñ‡ÐµÑ€ÐµÐ· API.
>
> ### 2\. Ð¤Ð¾ÐºÑƒÑÑƒÐ¹Ñ‚ÐµÑÑ Ð½Ð° Ð²Ð°ÑˆÑ–Ð¹ ÑÑ„ÐµÑ€Ñ– Ñ–Ð½Ñ‚ÐµÑ€ÐµÑÑ–Ð².
>
> ÐŸÐ°Ð½ÐµÐ»ÑŒ "Ð†Ð½ÑÑ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ð¸ Ð¼Ð°Ð»ÑŽÐ²Ð°Ð½Ð½Ñ" Ð´Ð¾Ð·Ð²Ð¾Ð»ÑÑ” Ð¼Ð°Ð»ÑŽÐ²Ð°Ñ‚Ð¸ Ð°Ð±Ð¾ Ð·Ð°Ð²Ð°Ð½Ñ‚Ð°Ð¶ÑƒÐ²Ð°Ñ‚Ð¸ Ð²Ð»Ð°ÑÐ½Ñƒ Ð³ÐµÐ¾Ð¼ÐµÑ‚Ñ€Ñ–ÑŽ Ð½Ð° ÐºÐ°Ñ€Ñ‚Ñƒ. Ð’Ð¸ Ñ‚Ð°ÐºÐ¾Ð¶ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ Ñ„Ð¾ÐºÑƒÑÑƒÐ²Ð°Ñ‚Ð¸ÑÑ Ð½Ð° Ð¾Ð±Ð»Ð°ÑÑ‚Ñ–, Ñ‰Ð¾ Ð¿Ð¾ÑÑ‚Ñ€Ð°Ð¶Ð´Ð°Ð»Ð° Ð²Ñ–Ð´ ÑÑ‚Ð¸Ñ…Ñ–Ð¹Ð½Ð¾Ð³Ð¾ Ð»Ð¸Ñ…Ð°, Ð°Ð±Ð¾ Ð½Ð° Ð°Ð´Ð¼Ñ–Ð½Ñ–ÑÑ‚Ñ€Ð°Ñ‚Ð¸Ð²Ð½Ñ–Ð¹ Ð¾Ð´Ð¸Ð½Ð¸Ñ†Ñ– â€” ÐºÑ€Ð°Ñ—Ð½Ð°, Ñ€Ð°Ð¹Ð¾Ð½ Ñ‡Ð¸ Ð¼Ñ–ÑÑ‚Ð¾.
>
> ### 3\. ÐžÑ‚Ñ€Ð¸Ð¼Ð°Ð¹Ñ‚Ðµ Ð°Ð½Ð°Ð»Ñ–Ñ‚Ð¸ÐºÑƒ Ð´Ð»Ñ Ð²Ð¸Ð±Ñ€Ð°Ð½Ð¾Ñ— Ð¾Ð±Ð»Ð°ÑÑ‚Ñ–.
>
> ÐŸÐ°Ð½ÐµÐ»ÑŒ ÐÐ½Ð°Ð»Ñ–Ñ‚Ð¸ÐºÐ° Ð¿Ð¾ÐºÐ°Ð·ÑƒÑ” ÐºÑ–Ð»ÑŒÐºÑ–ÑÑ‚ÑŒ Ð»ÑŽÐ´ÐµÐ¹, ÑÐºÑ– Ð¿Ñ€Ð¾Ð¶Ð¸Ð²Ð°ÑŽÑ‚ÑŒ Ñƒ Ñ†ÑŒÐ¾Ð¼Ñƒ Ñ€ÐµÐ³Ñ–Ð¾Ð½Ñ– Ð½Ð° Ð¾ÑÐ½Ð¾Ð²Ñ– [Ð½Ð°ÑÐµÐ»ÐµÐ½Ð½Ñ Kontur](https://data.humdata.org/dataset/kontur-population-dataset) Ñ– Ð¿Ð¾Ñ‚ÐµÐ½Ñ†Ñ–Ð¹Ð½Ñ– Ð¿Ñ€Ð¾Ð³Ð°Ð»Ð¸Ð½Ð¸ ÐºÐ°Ñ€Ñ‚Ð¾Ð³Ñ€Ð°Ñ„ÑƒÐ²Ð°Ð½Ð½Ñ Ð² OpenStreetMap. ÐšÐ»Ñ–Ñ”Ð½Ñ‚Ð¸ Kontur Ð¼Ð°ÑŽÑ‚ÑŒ Ð´Ð¾ÑÑ‚ÑƒÐ¿ Ð´Ð¾ ÑÐ¾Ñ‚ÐµÐ½ÑŒ Ñ–Ð½ÑˆÐ¸Ñ… Ð¿Ð¾ÐºÐ°Ð·Ð½Ð¸ÐºÑ–Ð² Ð·Ð° Ð´Ð¾Ð¿Ð¾Ð¼Ð¾Ð³Ð¾ÑŽ Ð”ÐµÑ‚Ð°Ð»ÑŒÐ½Ð¾Ñ— ÐÐ½Ð°Ð»Ñ–Ñ‚Ð¸ÐºÐ¸.
>
> ### 4\. Ð”Ð¾ÑÐ»Ñ–Ð´Ð¶ÑƒÐ¹Ñ‚Ðµ Ð´Ð°Ð½Ñ– Ð½Ð° ÐºÐ°Ñ€Ñ‚Ñ– Ñ‚Ð° Ñ€Ð¾Ð±Ñ–Ñ‚ÑŒ Ð²Ð¸ÑÐ½Ð¾Ð²ÐºÐ¸.
>
> ÐŸÐ°Ð½ÐµÐ»ÑŒ "Ð¨Ð°Ñ€Ð¸" Ð½Ð°Ð´Ð°Ñ” Ñ€Ñ–Ð·Ð½Ñ– Ð¿Ð°Ñ€Ð°Ð¼ÐµÑ‚Ñ€Ð¸ Ð´Ð»Ñ Ð¾Ð´Ð½Ð¾Ñ‡Ð°ÑÐ½Ð¾Ð³Ð¾ Ð²Ñ–Ð´Ð¾Ð±Ñ€Ð°Ð¶ÐµÐ½Ð½Ñ Ð´Ð²Ð¾Ñ… Ñ–Ð½Ð´Ð¸ÐºÐ°Ñ‚Ð¾Ñ€Ñ–Ð² Ð½Ð° ÐºÐ°Ñ€Ñ‚Ñ–, Ð½Ð°Ð¿Ñ€Ð¸ÐºÐ»Ð°Ð´, Ñ‰Ñ–Ð»ÑŒÐ½Ñ–ÑÑ‚ÑŒ Ð½Ð°ÑÐµÐ»ÐµÐ½Ð½Ñ Ñ‚Ð° Ð²Ñ–Ð´ÑÑ‚Ð°Ð½ÑŒ Ð´Ð¾ Ð½Ð°Ð¹Ð±Ð»Ð¸Ð¶Ñ‡Ð¾Ñ— Ð¿Ð¾Ð¶ÐµÐ¶Ð½Ð¾Ñ— Ñ‡Ð°ÑÑ‚Ð¸Ð½Ð¸. Ð’Ð¸ÐºÐ¾Ñ€Ð¸ÑÑ‚Ð¾Ð²ÑƒÐ¹Ñ‚Ðµ Ð»ÐµÐ³ÐµÐ½Ð´Ñƒ ÐºÐ¾Ð»ÑŒÐ¾Ñ€Ñ–Ð², Ñ‰Ð¾Ð± Ð²Ð¸Ð·Ð½Ð°Ñ‡Ð¸Ñ‚Ð¸, ÑÐºÑ– Ð¾Ð±Ð»Ð°ÑÑ‚Ñ– Ð¿Ð¾Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑŒ ÑƒÐ²Ð°Ð³Ð¸.
> ÐŸÑ–Ð´ÐºÐ°Ð·ÐºÐ°: Ð·Ð°Ð³Ð°Ð»Ð¾Ð¼ Ð·ÐµÐ»ÐµÐ½Ð¸Ð¹ ÐºÐ¾Ð»Ñ–Ñ€ Ð¾Ð·Ð½Ð°Ñ‡Ð°Ñ” Ð½Ð¸Ð·ÑŒÐºÐ¸Ð¹ Ñ€Ð¸Ð·Ð¸Ðº / Ð¼Ð°Ð»Ð¾ Ð¿Ñ€Ð¾Ð³Ð°Ð»Ð¸Ð½, Ñ‡ÐµÑ€Ð²Ð¾Ð½Ð¸Ð¹ â€” Ð²Ð¸ÑÐ¾ÐºÐ¸Ð¹ Ñ€Ð¸Ð·Ð¸Ðº / Ð±Ð°Ð³Ð°Ñ‚Ð¾ Ð¿Ñ€Ð¾Ð³Ð°Ð»Ð¸Ð½.

ÐšÑ€Ñ–Ð¼ Ñ‚Ð¾Ð³Ð¾, Ð²Ð¸ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ Ð¿ÐµÑ€ÐµÐ¹Ñ‚Ð¸ Ð´Ð¾ Ð·Ð²Ñ–Ñ‚Ñ–Ð² Ð½Ð° Ð»Ñ–Ð²Ñ–Ð¹ Ð¿Ð°Ð½ÐµÐ»Ñ–, Ñ‰Ð¾Ð± Ð¾Ñ‚Ñ€Ð¸Ð¼Ð°Ñ‚Ð¸ Ð´Ð¾ÑÑ‚ÑƒÐ¿ Ð´Ð¾ Ð´Ð°Ð½Ð¸Ñ… Ð¿Ñ€Ð¾ Ð¿Ð¾Ñ‚ÐµÐ½Ñ†Ñ–Ð¹Ð½Ñ– Ð¿Ð¾Ð¼Ð¸Ð»ÐºÐ¸ Ñ‚Ð° Ð½ÐµÐ²Ñ–Ð´Ð¿Ð¾Ð²Ñ–Ð´Ð½Ð¾ÑÑ‚Ñ– Ð² Ð´Ð°Ð½Ð¸Ñ… OpenStreetMap Ñ– Ð´Ð¾Ð¿Ð¾Ð¼Ð¾Ð³Ñ‚Ð¸ Ñ—Ñ… Ð²Ð¸Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ð¸, Ð½Ð°Ð¿Ñ€Ð¸ÐºÐ»Ð°Ð´ Ð·Ð° Ð´Ð¾Ð¿Ð¾Ð¼Ð¾Ð³Ð¾ÑŽ Ñ€ÐµÐ´Ð°ÐºÑ‚Ð¾Ñ€Ð° JOSM.

### [ÐŸÐµÑ€ÐµÐ¹Ñ‚Ð¸ Ð´Ð¾ Ð¼Ð°Ð¿Ð¸ âžœ](/ "map")

ÐœÐ¸ ÑÐ¿Ð¾Ð´Ñ–Ð²Ð°Ñ”Ð¼Ð¾ÑÑ, Ñ‰Ð¾ Ñ†ÐµÐ¹ Ñ–Ð½ÑÑ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚ Ð±ÑƒÐ´Ðµ ÐºÐ¾Ñ€Ð¸ÑÐ½Ð¸Ð¹. Ð’Ð¸ÐºÐ¾Ñ€Ð¸ÑÑ‚Ð¾Ð²ÑƒÐ¹Ñ‚Ðµ Ð²Ñ–ÐºÐ½Ð¾ Ñ‡Ð°Ñ‚Ñƒ Ð½Ð° Disaster Ninja, Ñ‰Ð¾Ð± Ð·Ð°Ð´Ð°Ñ‚Ð¸ Ð±ÑƒÐ´ÑŒ-ÑÐºÑ– Ð·Ð°Ð¿Ð¸Ñ‚Ð°Ð½Ð½Ñ Ñ‰Ð¾Ð´Ð¾ Ñ„ÑƒÐ½ÐºÑ†Ñ–Ð¾Ð½Ð°Ð»ÑŒÐ½Ð¾ÑÑ‚Ñ–, Ñ– Ð¼Ð¸ Ð· Ñ€Ð°Ð´Ñ–ÑÑ‚ÑŽ Ð´Ð¾Ð¿Ð¾Ð¼Ð¾Ð¶ÐµÐ¼Ð¾ Ð²Ð°Ð¼. Ð’Ð¸ Ñ‚Ð°ÐºÐ¾Ð¶ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ Ð·Ð²â€™ÑÐ·Ð°Ñ‚Ð¸ÑÑ Ð· Ð½Ð°Ð¼Ð¸ ÐµÐ»ÐµÐºÑ‚Ñ€Ð¾Ð½Ð½Ð¾ÑŽ Ð¿Ð¾ÑˆÑ‚Ð¾ÑŽ [hello@kontur.io](mailto:hello@kontur.io) ÑÐºÑ‰Ð¾ Ñƒ Ð²Ð°Ñ Ñ” Ð²Ñ–Ð´Ð³ÑƒÐºÐ¸ Ñ‡Ð¸ Ð¿Ñ€Ð¾Ð¿Ð¾Ð·Ð¸Ñ†Ñ–Ñ— Ñ‰Ð¾Ð´Ð¾ Ð²Ð´Ð¾ÑÐºÐ¾Ð½Ð°Ð»ÐµÐ½Ð½Ñ Ñ–Ð½ÑÑ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñƒ.

Disaster Ninja Ñ” Ð¿Ñ€Ð¾Ñ”ÐºÑ‚Ð¾Ð¼ Ð· Ð²Ñ–Ð´ÐºÑ€Ð¸Ñ‚Ð¸Ð¼ ÐºÐ¾Ð´Ð¾Ð¼. Ð”Ð¸Ð²Ñ–Ñ‚ÑŒÑÑ ÐºÐ¾Ð´ Ð² Ð¾Ð±Ð»Ñ–ÐºÐ¾Ð²Ð¾Ð¼Ñƒ Ð·Ð°Ð¿Ð¸ÑÑ– GitHub [Kontur](https://github.com/konturio).'::bytea);

--insert DN About page in Spanish
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Spanish',
            'es',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Â¡Bienvenido a Disaster Ninja!
=============================

Â¿Desea recibir notificaciones sobre desastres en curso? Â¿Le interesan los datos de poblaciÃ³n instantÃ¡neos y otros datos analÃ­ticos de alguna regiÃ³n del mundo? Disaster Ninja muestra algunas de las capacidades de [Kontur](https://www.kontur.io/) para ocuparse de estas necesidades.

Inicialmente la diseÃ±amos como una herramienta de apoyo a los mapeadores de servicios humanitarios. Ahora ha ampliado sus funcionalidades y aplicaciones prÃ¡cticas. Tanto si trabaja en la gestiÃ³n de desastres como si construye una ciudad inteligente o realiza investigaciones sobre el cambio climÃ¡tico, Disaster Ninja puede ayudarle a:

> ### 1\. Estar al dÃ­a con los Ãºltimos eventos peligrosos a nivel mundial.
>
> El panel de Desastres se actualiza continuamente para informarle sobre los acontecimientos en curso. Utiliza la informaciÃ³n proporcionada por [Kontur Event Feed](https://www.kontur.io/portfolio/event-feed/), a la que tambiÃ©n puede acceder a travÃ©s de la interfaz de la aplicaciÃ³n.
>
> ### 2\. Centrarse en su Ã¡rea de interÃ©s.
>
> El panel de Herramientas de Dibujo le permite dibujar o subir su propia geometrÃ­a en el mapa. TambiÃ©n puede centrarse en un Ã¡rea o en una unidad administrativa - paÃ­s, ciudad o regiÃ³n â€” expuesta a desastres.
>
> ### 3\. Recibir anÃ¡lisis de su Ã¡rea de interÃ©s.
>
> El panel de AnÃ¡lisis le muestra el nÃºmero de personas que viven en la zona segÃºn [Kontur Population](https://data.humdata.org/dataset/kontur-population-dataset) y segÃºn una estimaciÃ³n de las zonas sin mapear en OpenStreetMap. Los clientes de Kontur tienen acceso a cientos de otros indicadores a travÃ©s de Advance Analytics.
>
> ### 4\. Explorar los datos del mapa y sacar sus propias conclusiones.
>
> El panel Capas le ofrece varias opciones para mostrar dos indicadores en simultÃ¡neo en un mapa bivariado, p. ej:, la densidad de poblaciÃ³n y la distancia a la estaciÃ³n de bomberos mÃ¡s cercana. Utilice las leyendas de colores para evaluar quÃ© Ã¡reas requieren atenciÃ³n.
> Consejo: en general, el verde indica riesgo bajo / pocas zonas sin mapear, el rojo â€” riesgo alto / muchas zonas sin mapear.

AdemÃ¡s, puede cambiar a Informes en el panel de la izquierda para acceder a los datos sobre posibles errores e inconsistencias en OpenStreetMap y ayudarle a realizar correcciones mapeando la zona correspondiente con el editor JOSM.

### [Ir al mapa ahora âžœ](/ "map")

Esperamos que esta herramienta le resulte valiosa. Utilice el chat de Disaster Ninja si tiene alguna pregunta sobre sus funcionalidades y estaremos encantados de ayudarle. TambiÃ©n puede contactarnos por correo electrÃ³nico en [hello@kontur.io](mailto:hello@kontur.io) si tiene algÃºn comentario o sugerencia para mejorar la herramienta.

Disaster Ninja es un proyecto de cÃ³digo abierto. Encuentre el cÃ³digo en la [cuenta de GitHub de Kontur](https://github.com/konturio).'::bytea);
