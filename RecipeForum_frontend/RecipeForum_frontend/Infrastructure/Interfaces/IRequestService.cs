namespace RecipeForum_frontend.Infrastructure.Interfaces
{
    public interface IRequestService
    {
        Task<bool> LoginAsync(string username, string password);
        Task LogoutAsync();
    }
}
