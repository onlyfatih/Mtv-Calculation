@chrome @firefox
Feature: MTV Calculation Based on Vehicle Parameters

  Background:
    Given user navigates to MTV calculator page

  @standard
  Scenario Outline: Calculate MTV for standard vehicle types with minimal required inputs
    When user selects "<vehicleType>" in "Araç Tipi"
    And user selects "<vehicleAge>" in "Araç Yaşı"
    And user selects "<fieldValue>" for "<fieldLabel>"
    And user clicks the Calculate button
    Then the result should be displayed
    And the total payment should be "<totalAmount>"
    And the first half-year payment should be "<firstHalf>"
    And the second half-year payment should be "<secondHalf>"

    Examples:
      | vehicleType                              | vehicleAge | fieldLabel            | fieldValue         | totalAmount | firstHalf   | secondHalf  |
      | Motosikletler                            | 1-3        | Motor Silindir Hacmi  | 100-250            | ₺899,00     | ₺449,50     | ₺449,50     |
      | Motosikletler                            | 4-6        | Motor Silindir Hacmi  | 651-1.200          | ₺2.857,00   | ₺1.428,50   | ₺1.428,50   |
      | Motosikletler                            | 12-15      | Motor Silindir Hacmi  | 1.201 ve üstü      | ₺3.817,00   | ₺1.908,50   | ₺1.908,50   |
      | Minibüsler                               | 1-6        | Koltuk Sayısı         | 0-17               | ₺5.780,00   | ₺2.890,00   | ₺2.890,00   |
      | Otobüs ve Benzerleri                     | 7-15       | Koltuk Sayısı         | 46 ve üstü         | ₺19.489,00  | ₺9.744,50   | ₺9.744,50   |
      | Kamyon - Kamyonet - Çekici ve Benzerleri | 1-6        | Azami Toplam Ağırlık  | 5.001-10.000       | ₺17.513,00  | ₺8.756,50   | ₺8.756,50   |
      | Uçak ve Helikopterler                    | 4-5        | Azami Kalkış Ağırlığı | 5.001 KG-10.000 KG | ₺234.759,00 | ₺117.379,50 | ₺117.379,50 |
      | Elektrikli Motosikletler                 | 7-11       | Kilowatt (kW)         | 41 kW-60 kW        | ₺352,00     | ₺176,00     | ₺176,00     |

  @dynamic-fields
  Scenario Outline: Calculate MTV for automobiles with additional dependent fields
    When user selects "<vehicleType>" in "Araç Tipi"
    And user selects "<vehicleAge>" in "Araç Yaşı"
    And user selects "<fieldValue>" for "<fieldLabel>"
    And user selects "<fieldValue1>" for "<fieldLabel1>"
    And user selects "<fieldValue2>" for "<fieldLabel2>"
    And user clicks the Calculate button
    Then the result should be displayed
    And the total payment should be "<totalAmount>"
    And the first half-year payment should be "<firstHalf>"
    And the second half-year payment should be "<secondHalf>"

    Examples:
      | vehicleType                                                   | vehicleAge | fieldLabel           | fieldValue      | fieldLabel1     | fieldValue1 | fieldLabel2 | fieldValue2           | totalAmount | firstHalf  | secondHalf |
      | Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri            | 4-6        | Motor Silindir Hacmi | 2.001 - 2.500   | İlk Tescil Yılı | 2018        | Araç Değeri | 125.000,01 TL ve üstü | ₺30.642,00  | ₺15.321,00 | ₺15.321,00 |
      | Elektrikli Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri | 1-3        | Kilowatt (kW)        | 181 kW - 210 kW | İlk Tescil Yılı | 2023        | Araç Değeri | 714.300,01 TL ve üstü | ₺22.413,00  | ₺11.206,50 | ₺11.206,50 |

  @visibility
  Scenario Outline: Verify visibility of dropdown fields based on selected vehicle type
    When user selects "<vehicleType>" in "Araç Tipi"
    Then "<dropdown1Label>" dropdown should be <dropdown1Visibility>
    And "<dropdown2Label>" dropdown should be <dropdown2Visibility>
    And "<dropdown3Label>" dropdown should be <dropdown3Visibility>

    Examples:
      | vehicleType                                                   | dropdown1Label       | dropdown1Visibility | dropdown2Label  | dropdown2Visibility | dropdown3Label       | dropdown3Visibility |
      | Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri            | İlk Tescil Yılı      | visible             | Araç Yaşı       | visible             | Motor Silindir Hacmi | visible             |
      | Motosikletler                                                 | İlk Tescil Yılı      | hidden              | Araç Değeri     | hidden              | Motor Silindir Hacmi | visible             |
      | Minibüsler                                                    | Koltuk Sayısı        | visible             | İlk Tescil Yılı | hidden              | Araç Yaşı            | visible             |
      | Otobüs ve Benzerleri                                          | Koltuk Sayısı        | visible             | İlk Tescil Yılı | hidden              | Araç Yaşı            | visible             |
      | Kamyon - Kamyonet - Çekici ve Benzerleri                      | Azami Toplam Ağırlık | visible             | İlk Tescil Yılı | hidden              | Araç Yaşı            | visible             |
      | Elektrikli Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri | Kilowatt (kW)        | visible             | İlk Tescil Yılı | visible             | Araç Yaşı            | visible             |
      | Motosikletler                                                 | Koltuk Sayısı        | hidden              | Araç Değeri     | hidden              | Motor Silindir Hacmi | visible             |
      | Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri            | Koltuk Sayısı        | hidden              | İlk Tescil Yılı | visible             | Azami Toplam Ağırlık | hidden              |

  @negative @validation @edge
  Scenario Outline: Validate error messages when required fields are left empty
    When user selects "<vehicleType>" in "Araç Tipi"
    And user selects "<vehicleAge>" in "Araç Yaşı"
    And user clicks the Calculate button
    Then the result should not be displayed
    And error message "<expectedErrorMessage>" should be visible

    Examples:
      | vehicleType                                        | vehicleAge | expectedErrorMessage                        |
      | Motosikletler                                      | 1-3        | Motor Silindir Hacmi Alanı Boş Bırakılamaz. |
      | Elektrikli Panel Van ve Motorlu Karavanlar         | 1-6        | Kilowatt (kW) Alanı Boş Bırakılamaz.        |
      | Otomobil - Kaptıkaçtı - Arazi Taşıtı ve Benzerleri | 1-3        | Motor Silindir Hacmi Alanı Boş Bırakılamaz. |
      | Minibüsler                                         | 1-6        | Koltuk Sayısı Alanı Boş Bırakılamaz.        |
