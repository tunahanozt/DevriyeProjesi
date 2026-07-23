namespace DevriyeTakip.API.Models
{
    public class Checkpoint
    {
        public int Id { get; set; }

        // Bölge hiyerarşisi için (Opsiyonel olarak sonradan Zone tablosuna bağlanabilir)
        public int ZoneId { get; set; }
        public Zone Zone { get; set; }

        public string Name { get; set; } = string.Empty; // Örn: "Kazan Dairesi Girişi"

        // Fiziksel etiketin donanım ID'si. Mobil uygulamadan okuyacağımız değer tam olarak bu!
        public string NfcUID { get; set; } = string.Empty;

        public bool IsActive { get; set; } = true;
    }
}