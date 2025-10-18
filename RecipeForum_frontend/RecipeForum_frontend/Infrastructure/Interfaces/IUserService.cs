namespace RecipeForum_frontend.Infrastructure.Interfaces
{
    public interface IUserService
    {
        Task<bool> IsUserLoggedIn();
    }
}
