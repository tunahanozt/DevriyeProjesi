using DevriyeTakip.API.Models;

namespace DevriyeTakip.API.Repositories
{
    public interface ICheckpointRepository
    {
        // Yönetim paneli için tüm noktaları getiren metodun kontratı
        Task<IEnumerable<Checkpoint>> GetAllAsync();

        // Mobil uygulama için UID'ye göre aktif noktayı bulan metodun kontratı
        Task<Checkpoint?> GetActiveByUidAsync(string nfcUid);
    }
}