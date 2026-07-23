namespace DevriyeTakip.API.Models
{
    public class Zone
    {
        public int Id { get; set; }
        public string ZoneName { get; set; } = string.Empty; // Örn: "A Blok"
        public int? ParentZoneId { get; set; } // Üst bölgesi varsa
    }
}