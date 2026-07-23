namespace DevriyeTakip.API.Models
{
    public class User
    {
        public int Id { get; set; }
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public string RegistrationNumber { get; set; } = string.Empty; // Sicil Numarası

        // Gerçek bir senaryoda şifreler düz metin tutulmaz, Hashlenir.
        public string PasswordHash { get; set; } = string.Empty;
        public int RoleId { get; set; }
        public Role Role { get; set; }
        public bool IsActive { get; set; } = true;
    }
}