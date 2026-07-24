namespace DevriyeTakip.API.Models
{
    // Bir devriyenin yaşam döngüsündeki durumu.
    public enum PatrolStatus
    {
        Atandi = 0,        // Personele atandı, henüz başlamadı
        Basladi = 1,       // Devriye başladı
        Tamamlandi = 2,    // Tüm noktalar okutuldu, tamamlandı
        YaridaKesildi = 3  // Yarıda bırakıldı (sebep girilir)
    }
}
