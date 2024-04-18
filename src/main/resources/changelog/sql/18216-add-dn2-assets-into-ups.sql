--liquibase formatted sql

--changeset user-profile-service:18216-add-dn2-assets-into-ups.sql runOnChange:true

--insert DN About page in Arabic
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Arabic',
            'ar',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'مرحبًا في Disaster Ninja!
=========================

هل تريد أن يتم إخطارك بالكوارث الجارية؟ هل أنت مهتم بالبيانات السكانية الفورية والتحليلات الأخرى لأي منطقة في العالم؟ يعرض Disaster Ninja بعض قدرات [Kontur](https://www.kontur.io/) في تلبية هذه الاحتياجات.

لقد صممناه في البداية ليكون أداة دعم قرار لمصممي الخرائط الإنسانية. الآن، ازدادت وظائفه وحالات استخدامه. سواء كنت تعمل في إدارة الكوارث، أو تبني مدينة ذكية، أو تجري بحثًا عن تغير المناخ، يمكن أن يساعدك Disaster Ninja على:

> ### 1. البقاء على اطلاع بآخر التطورات للأحداث الخطرة على مستوى العالم.
>
> يتم تحديث لوحة الكوارث باستمرار لإبلاغك بالأحداث الجارية. إنها تستهلك البيانات من [Kontur Event Feed](https://www.kontur.io/portfolio/event-feed/) والتي يمكنك أيضًا الوصول إليها عبر واجهة برمجة التطبيقات.
>
> ### 2. التركيز على مجال اهتمامك.
>
> تتيح لك لوحة أدوات الرسم رسم أو تحميل الشكل الهندسي الخاص بك على الخريطة. يمكنك أيضًا التركيز على منطقة معرضة للكوارث أو وحدة إدارية - بلد أو مدينة أو منطقة.
>
> ### 3. الحصول على تحليلات لمنطقة الاهتمام.
>
> تعرض لوحة التحليلات عدد الأشخاص الذين يعيشون في تلك المنطقة حسب [Kontur Population](https://data.humdata.org/dataset/kontur-population-dataset) وفجوات الخرائط المقدرة في OpenStreetMap. يمكن لعملاء Kontur الوصول إلى مئات المؤشرات الأخرى من خلال التحليلات المتقدمة.
>
> ### 4. استكشاف البيانات على الخريطة والتوصل إلى استنتاجات.
>
> تمنحك لوحة Layers خيارات متنوعة لعرض مؤشرين في وقت واحد على خريطة ثنائية المتغير على سبيل المثال الكثافة السكانية والمسافة إلى أقرب محطة إطفاء. استخدم وسيلة إيضاح اللون لتقييم المناطق التي تتطلب الانتباه.
> بشكل عام، يشير اللون الأخضر إلى مخاطر منخفضة / فجوات قليلة، والأحمر - مخاطر عالية / العديد من الفجوات.

بالإضافة إلى ذلك ، يمكنك التبديل إلى التقارير في اللوحة اليمنى للوصول إلى البيانات المتعلقة بالأخطاء والتناقضات المحتملة في OpenStreetMap والمساعدة في إصلاحها عن طريق رسم خريطة المنطقة المعنية باستخدام محرر JOSM.

### [الانتقال إلى الخريطة الآن ➜](/ "map")

نأمل أن تجد هذه الأداة ذات قيمة. استخدم مربع الدردشة على Disaster Ninja لأية أسئلة حول وظائفه، وسنكون سعداء بإرشادك. يمكنك أيضًا التواصل معنا عبر البريد الإلكتروني[hello@kontur.io](mailto:hello@kontur.io) إذا كان لديك ملاحظات أو اقتراحات بشأن تحسين الأداة.

يُعدّ Disaster Ninja مشروعًا مفتوح المصدر. تحقق من الكود في حساب [Kontur](https://github.com/konturio) على [GitHub](https://github.com/konturio).'::bytea);

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

> ### 1. Selalu mendapatkan kabar terbaru tentang kejadian berbahaya terkini di seluruh dunia.
>
> Panel Bencana disegarkan secara terus-menerus untuk memberi tahu Anda kejadian yang sedang berlangsung. Panel ini memakai data dari [Feed Kejadian Kontur](https://www.kontur.io/portfolio/event-feed/), yang juga dapat diakses melalui API.
>
> ### 2. Fokus pada area perhatian Anda.
>
> Panel Peralatan Gambar memungkinkan Anda menggambar atau mengunggah geometri Anda sendiri pada peta. Anda juga dapat berfokus pada area yang terpapar bencana atau satuan administratif — negara, kota, atau wilayah.
>
> ### 3. Dapatkan analitik untuk area yang menjadi fokus.
>
> Panel Analitik memperlihatkan jumlah orang yang tinggal di area tersebut untuk setiap [Populasi Kontur](https://data.humdata.org/dataset/kontur-population-dataset) dan perkiraan kesenjangan pemetaan di OpenStreetMap. Pelanggan Kontur memiliki akses ke ratusan indikator lainnya melalui Analitik Lanjut.
>
> ### 4. Selidiki data pada peta dan buat kesimpulan.
>
> Panel Lapisan memberi Anda berbagai opsi untuk menampilkan dua indikator secara serentak pada peta bivariat, misalnya kepadatan populasi dan jarak ke stasiun pemadam kebakaran terdekat. Gunakan legenda warna untuk menilai area mana yang perlu diperhatikan.
> Petunjuk: secara umum, warna hijau menunjukkan risiko rendah/sedikit kesenjangan, warna merah — risiko tinggi/banyak kesenjangan.

Selain itu, Anda dapat beralih ke Laporan di panel kiri untuk mengakses data tentang potensi kesalahan dan inkonsistensi di OpenStreetMap serta membantu memperbaikinya dengan memetakan masing-masing area menggunakan editor JOSM.

### [Buka peta sekarang ➜](/ "map")

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
            'Disaster Ninja에 오신 것을 환영합니다!
============================

현재 진행 중인 재난에 대해 알림을 받아보시겠습니까? 전 세계 모든 지역의 인구 데이터와 기타 분석 사항을 즉시 알아보고 싶으신가요? Disaster Ninja에서는 이러한 요구 사항을 해결하기 위해 몇 가지 [Kontur](https://www.kontur.io/) 기능을 사용합니다.

처음에는 인도주의적인 문제를 다루기 위한 의사 결정 지원 도구로 고안되었지만, 이제는 기능과 용법이 여러 가지로 늘어났습니다. 재난 관리, 스마트 시티 구축, 기후 변화에 관한 연구 수행 등 어떤 업무를 하더라도 다음과 같은 도움을 드릴 수 있습니다.

> ### 1. 전 세계의 최신 위험 이벤트의 동향을 알려 드립니다.
>
> 재난 패널이 지속적으로 새로 고침 되어 현재 일어나는 이벤트에 대한 정보를 제공합니다. [Kontur 이벤트 피드](https://www.kontur.io/portfolio/event-feed/)의 데이터를 사용하며, API를 통해서도 해당 데이터에 액세스할 수 있습니다.
>
> ### 2. 관심 영역을 집중적으로 보여 드립니다.
>
> 그리기 도구 패널을 사용하면 자체 기하 도형을 지도에 그리거나 업로드할 수 있습니다. 재난에 노출된 영역이나 행정 단위(예: 국가, 도시, 지역)에 초점을 맞출 수도 있습니다.
>
> ### 3. 집중 영역에 대한 분석을 제공합니다.
>
> 분석 패널은 [Kontur 인구](https://data.humdata.org/dataset/kontur-population-dataset)당 해당 영역에 거주하는 사람의 수와 OpenStreetMap 내 예상 매핑 갭을 보여줍니다. 그 외에도 Kontur 고객은 고급 분석을 통해 수백 가지 지표에 액세스할 수 있습니다.
>
> ### 4. 지도의 데이터를 탐색하여 결론을 내릴 수 있습니다.
>
> 레이어 패널을 사용하면 이변수 지도에서 두 가지 지표를 동시에 표시하는 다양한 옵션을 사용할 수 있게 됩니다(예: 인구 밀도, 가장 가까운 소방서까지의 거리). 색상 범례를 사용하여 주의가 필요한 영역을 평가하세요.
> 힌트: 일반적으로 녹색은 저위험/적은 갭을 의미하고 빨간색은 고위험/많은 갭을 의미합니다.

또한, 왼쪽 패널의 보고서로 이동하여 OpenStreetMap의 잠재적인 오류 및 불일치 데이터에 액세스하고, JOSM 편집기를 통해 각 영역을 매핑하여 해당 데이터를 수정할 수도 있습니다.

### [지금 바로 지도로 이동하세요. ➜](/ "map")

이 도구가 많은 도움이 되기를 바랍니다. 기능에 대해 궁금한 점은 Disaster Ninja의 챗박스를 통해 언제든지 문의해 주시면 도와 드리겠습니다. 도구 개선에 대한 피드백이나 제안 사항이 있을 경우, [hello@kontur.io](mailto:hello@kontur.io) 로 이메일 주셔도 됩니다.

Disaster Ninja는 오픈 소스 프로젝트입니다. [Kontur의 GitHub 계정](https://github.com/konturio)에서 코드를 찾아보세요.'::bytea);

--insert DN About page in Ukrainian
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Ukrainian',
            'uk',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'Вітаємо на Disaster Ninja!
==========================

Ви хочете отримувати повідомлення про катастрофи? Вас цікавлять миттєві дані про населення та інша аналітика для будь-якого регіону світу? Disaster Ninja від [Kontur](https://www.kontur.io/) може задовольнити ці потреби.

Ми розробили це як інструмент підтримки прийняття рішень для гуманітарних картографів. Тепер його функціональність зросла. Незалежно від того, чи працюєте ви в сфері боротьби зі стихійними лихами, будуєте розумне місто чи проводите дослідження щодо зміни клімату, Disaster Ninja може вам допомогти:

> ### 1. Будьте в курсі останніх катастроф у всьому світі.
>
> Панель Катастроф постійно оновлюється, щоб інформувати вас про поточні події. Дані отримані з каналу подій [Kontur](https://www.kontur.io/portfolio/event-feed/), до якого також можна отримати доступ через API.
>
> ### 2. Фокусуйтеся на вашій сфері інтересів.
>
> Панель "Інструменти малювання" дозволяє малювати або завантажувати власну геометрію на карту. Ви також можете фокусуватися на області, що постраждала від стихійного лиха, або на адміністративній одиниці — країна, район чи місто.
>
> ### 3. Отримайте аналітику для вибраної області.
>
> Панель Аналітика показує кількість людей, які проживають у цьому регіоні на основі [населення Kontur](https://data.humdata.org/dataset/kontur-population-dataset) і потенційні прогалини картографування в OpenStreetMap. Клієнти Kontur мають доступ до сотень інших показників за допомогою Детальної Аналітики.
>
> ### 4. Досліджуйте дані на карті та робіть висновки.
>
> Панель "Шари" надає різні параметри для одночасного відображення двох індикаторів на карті, наприклад, щільність населення та відстань до найближчої пожежної частини. Використовуйте легенду кольорів, щоб визначити, які області потребують уваги.
> Підказка: загалом зелений колір означає низький ризик / мало прогалин, червоний — високий ризик / багато прогалин.

Крім того, ви можете перейти до звітів на лівій панелі, щоб отримати доступ до даних про потенційні помилки та невідповідності в даних OpenStreetMap і допомогти їх виправити, наприклад за допомогою редактора JOSM.

### [Перейти до мапи ➜](/ "map")

Ми сподіваємося, що цей інструмент буде корисний. Використовуйте вікно чату на Disaster Ninja, щоб задати будь-які запитання щодо функціональності, і ми з радістю допоможемо вам. Ви також можете зв’язатися з нами електронною поштою [hello@kontur.io](mailto:hello@kontur.io) якщо у вас є відгуки чи пропозиції щодо вдосконалення інструменту.

Disaster Ninja є проєктом з відкритим кодом. Дивіться код в обліковому записі GitHub [Kontur](https://github.com/konturio).'::bytea);

--insert DN About page in Spanish
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'markdown',
            'about.md',
            'Disaster Ninja About page in Spanish',
            'es',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            '¡Bienvenido a Disaster Ninja!
=============================

¿Desea recibir notificaciones sobre desastres en curso? ¿Le interesan los datos de población instantáneos y otros datos analíticos de alguna región del mundo? Disaster Ninja muestra algunas de las capacidades de [Kontur](https://www.kontur.io/) para ocuparse de estas necesidades.

Inicialmente la diseñamos como una herramienta de apoyo a los mapeadores de servicios humanitarios. Ahora ha ampliado sus funcionalidades y aplicaciones prácticas. Tanto si trabaja en la gestión de desastres como si construye una ciudad inteligente o realiza investigaciones sobre el cambio climático, Disaster Ninja puede ayudarle a:

> ### 1. Estar al día con los últimos eventos peligrosos a nivel mundial.
>
> El panel de Desastres se actualiza continuamente para informarle sobre los acontecimientos en curso. Utiliza la información proporcionada por [Kontur Event Feed](https://www.kontur.io/portfolio/event-feed/), a la que también puede acceder a través de la interfaz de la aplicación.
>
> ### 2. Centrarse en su área de interés.
>
> El panel de Herramientas de Dibujo le permite dibujar o subir su propia geometría en el mapa. También puede centrarse en un área o en una unidad administrativa - país, ciudad o región — expuesta a desastres.
>
> ### 3. Recibir análisis de su área de interés.
>
> El panel de Análisis le muestra el número de personas que viven en la zona según [Kontur Population](https://data.humdata.org/dataset/kontur-population-dataset) y según una estimación de las zonas sin mapear en OpenStreetMap. Los clientes de Kontur tienen acceso a cientos de otros indicadores a través de Advance Analytics.
>
> ### 4. Explorar los datos del mapa y sacar sus propias conclusiones.
>
> El panel Capas le ofrece varias opciones para mostrar dos indicadores en simultáneo en un mapa bivariado, p. ej:, la densidad de población y la distancia a la estación de bomberos más cercana. Utilice las leyendas de colores para evaluar qué áreas requieren atención.
> Consejo: en general, el verde indica riesgo bajo / pocas zonas sin mapear, el rojo — riesgo alto / muchas zonas sin mapear.

Además, puede cambiar a Informes en el panel de la izquierda para acceder a los datos sobre posibles errores e inconsistencias en OpenStreetMap y ayudarle a realizar correcciones mapeando la zona correspondiente con el editor JOSM.

### [Ir al mapa ahora ➜](/ "map")

Esperamos que esta herramienta le resulte valiosa. Utilice el chat de Disaster Ninja si tiene alguna pregunta sobre sus funcionalidades y estaremos encantados de ayudarle. También puede contactarnos por correo electrónico en [hello@kontur.io](mailto:hello@kontur.io) si tiene algún comentario o sugerencia para mejorar la herramienta.

Disaster Ninja es un proyecto de código abierto. Encuentre el código en la [cuenta de GitHub de Kontur](https://github.com/konturio).'::bytea);
