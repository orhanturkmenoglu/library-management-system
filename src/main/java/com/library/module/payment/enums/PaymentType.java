package com.library.module.payment.enums;

public enum PaymentType {

    // Anlamı: Para cezası
    // Kullanım: Kitabın geç teslim edilmesi durumunda uygulanır
    FINE,

    // Kullanım: Kütüphane üyelik işlemleri için alınan ödeme
    MEMBERSHIP,

    // Anlamı: Kayıp kitap cezası
    // Kullanım: Kullanıcının kitabı kaybetmesi durumunda uygulanır
    LOST_BOOK_PENALTY,

    // Anlamı: Hasarlı kitap cezası
    // Kullanım: Kitabın yırtık, ıslak veya zarar görmüş şekilde iade edilmesi durumunda uygulanır
    DAMAGED_BOOK_PENALTY,

    // Anlamı: Geri ödeme / iade
    // Kullanım: İptal edilen veya hatalı ödemelerin kullanıcıya geri verilmesi
    REFUND
}
