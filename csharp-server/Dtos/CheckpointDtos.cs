namespace DevriyeTakip.API.Dtos
{
    // İstek: yeni kontrol noktası oluşturma
    public class CreateCheckpointDto
    {
        public string Name { get; set; } = string.Empty;
        public string NfcUID { get; set; } = string.Empty;
        public int ZoneId { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string? Description { get; set; }
    }

    // Yanıt: dışarıya dönen sade nokta modeli (EF navigasyonları sızmaz)
    public class CheckpointDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string NfcUID { get; set; } = string.Empty;
        public int ZoneId { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string? Description { get; set; }
        public bool IsActive { get; set; }
    }
}
