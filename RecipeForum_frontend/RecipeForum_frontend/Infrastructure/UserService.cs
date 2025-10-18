using Blazored.LocalStorage;
using RecipeForum_frontend.Infrastructure.Interfaces;
using static RecipeForum_frontend.Constants.Constants;

namespace RecipeForum_frontend.Infrastructure
{
    public class UserService : IUserService
    {
        private ILocalStorageService _localStorageService;
        public UserService(ILocalStorageService storageService)
        {
            _localStorageService = storageService;
        }
        public async Task<bool> IsUserLoggedIn()
        {
            bool completed = await _localStorageService.GetItemAsync<string>(AuthTokenName) == "";
            return completed;
        }
    }
}
