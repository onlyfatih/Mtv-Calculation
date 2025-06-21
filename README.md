# MTV Hesaplama Test Otomasyonu

Bu proje, [Gelir İdaresi Başkanlığı'nın MTV Hesaplama](https://dijital.gib.gov.tr/hesaplamalar/MTVHesaplama) sayfasının test otomasyonunu gerçekleştirmek amacıyla hazırlanmıştır. Testler **Cucumber (Gherkin)** senaryoları kullanılarak yazılmış, **Selenium WebDriver** ile gerçekleştirilmiş ve **Allure Report** ile detaylı şekilde raporlanmıştır.

---

## 📦 Kullanılan Teknolojiler ve Yapı

| Bileşen             | Açıklama                                 |
|---------------------|------------------------------------------|
| Framework           | Cucumber + TestNG                        |
| Browser Otomasyonu  | Selenium WebDriver                       |
| Raporlama           | Allure Report                            |
| Driver Yönetimi     | WebDriverManager                         |
| Tarayıcılar         | Chrome, Firefox                          |
| Test Yapısı         | Page Object Model + LocatorReader        |
| Paralel Koşum       | TestNG + run-tests.sh Scripti            |


## 🧪 Test Senaryoları

Feature dosyasında tanımlı 4 ana senaryo grubu vardır:

-  `@standard`: Temel MTV hesaplama senaryoları
-  `@dynamic-fields`: Araç tipine göre açılan alanların doğrulanması
- ️ `@visibility`: Dinamik dropdown görünürlük kontrolleri
-  `@negative`, `@validation`, `@edge`: Boş alanlar ve hata mesajı testleri

---

## ⚙️ Testlerin Çalıştırılması

⚙️ Maven Bağımlılıklarını İndir

mvn clean install -DskipTests

🧪 Testlerin Çalıştırılması

🔹 Sadece Chrome ile test çalıştır:

bash run-tests.sh chrome

🔹 Sadece Firefox ile test çalıştır:

bash run-tests.sh firefox

🔹 Her iki tarayıcıda paralel test çalıştır:

bash run-tests.sh all

    Bu komut:

        target/allure-results/chrome ve target/allure-results/firefox klasörlerine test çıktıları oluşturur.

        Ardından bu klasörleri target/allure-report içinde birleştirerek Allure raporunu oluşturur ve otomatik olarak açar.

📊 Allure Raporu

Allure, tüm senaryoları kullanıcı dostu bir arayüzle sunar. Aşağıdaki bilgileri içerir:

    ✅ Her senaryonun geçme/kalma durumu

    🌐 Tarayıcı bilgisi, ortam adı, işletim sistemi

    📸 Hatalı adımlar için ekran görüntüleri

    🧾 Her adım için loglar ve süre bilgileri

Manuel olarak rapor oluşturmak istersen:

allure generate target/allure-results/chrome target/allure-results/firefox -o target/allure-report --clean
allure open target/allure-report

    bash run-tests.sh all, chrome, veya firefox komutları bu işlemleri zaten otomatik gerçekleştirir.

🕒 Timeline Sekmesi & Tarayıcı Bazlı Analiz

Allure raporundaki Timeline sekmesi sayesinde:

    Chrome ve Firefox testleri renk ayrımıyla görsel olarak ayırt edilebilir.

    Her testin tarayıcı bazlı süresi karşılaştırılabilir.

🧠 Gözlemler:

    🔵 Chrome: Daha hızlı render ve element yüklemesi → Testler daha stabil ve hızlıdır.

    🟠 Firefox: Bazı dropdown/render süreçlerinde gecikme olabilir → Görünürlük kontrolleri daha uzun sürebilir.

💡 Önerilen Test Stratejisi

    Öncelikle hızlıca doğrulama için:

bash run-tests.sh chrome

Sonrasında her iki tarayıcıda kıyaslama ve paralel test koşumu için:

    bash run-tests.sh all

    Böylece hem hızlı geri bildirim alınır hem de tarayıcıya özel farklar raporda net şekilde analiz edilebilir.

📌 Ek Notlar

    🔧 XPath tanımları MTVCalculatorPage.properties dosyasından okunur (dış kaynaklı yönetim).

    🔄 Hooks.java içinde Allure'a dinamik olarak tarayıcı, ortam ve sistem bilgileri eklenir.

    ⚙️ TestNGTestRunner.java içinde tüm parametreler dinamik olarak @BeforeClass aşamasında set edilir.

    🤝 Paralel koşum desteği için @DataProvider(parallel = true) yapılandırması kullanılmıştır.

📺 Test Otomasyon Video Kaydı

https://www.youtube.com/watch?v=qy1kFyKXDVs

    00:00 - 01:30 | Chrome ile testlerin çalıştırılması

    01:30 - 05:29 | Paralel testlerin başarılı şekilde çalıştırılması

    05:29 - 07:30 | Bilerek oluşturulan test hatasıyla Allure raporunda ekran görüntüsü ve grafiklerin gösterilmesi