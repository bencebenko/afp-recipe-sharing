namespace RecipeForum_frontend.Infrastructure.Interfaces
{
    public interface ITokenService
    {
        Task<string?> GetAccessTokenAsync();
        Task EnsureFreshTokenAsync();
        Task ClearTokensAsync();
    }
}
