using DevriyeTakip.API.Data;
using DevriyeTakip.API.Models;
using Microsoft.EntityFrameworkCore;

namespace DevriyeTakip.API.Repositories
{
    // Bu sınıf ICheckpointRepository kontratındaki kurallara uymak zorundadır
    public class CheckpointRepository : ICheckpointRepository
    {
        private readonly AppDbContext _context;

        public CheckpointRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Checkpoint>> GetAllAsync()
        {
            return await _context.Checkpoints.ToListAsync();
        }

        public async Task<Checkpoint?> GetActiveByUidAsync(string nfcUid)
        {
            return await _context.Checkpoints
                .FirstOrDefaultAsync(c => c.NfcUID == nfcUid && c.IsActive);
        }
    }
}