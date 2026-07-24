using DevriyeTakip.API.Dtos;

namespace DevriyeTakip.API.Services
{
    // Devriye (bir personele atanmış rota yürütmesi) iş kuralları.
    public interface IPatrolService
    {
        Task<PatrolDto?> AssignAsync(AssignPatrolDto dto);
        Task<IEnumerable<PatrolDto>> GetByUserAsync(int userId);
        Task<PatrolDto?> StartAsync(int patrolId);
        Task<PatrolDto?> CompleteAsync(int patrolId);
    }
}
