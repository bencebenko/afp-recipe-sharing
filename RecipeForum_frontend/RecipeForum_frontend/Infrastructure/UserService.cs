using Blazored.LocalStorage;
using Microsoft.AspNetCore.Http;
using Microsoft.JSInterop;
using RecipeForum_frontend.Infrastructure.Interfaces;
using static RecipeForum_frontend.Constants.Constants;

namespace RecipeForum_frontend.Infrastructure
{
    public class UserService : IUserService
    {
        private IJSRuntime _jsRuntime;
        public UserService(IJSRuntime jsRuntime)
        {
            _jsRuntime = jsRuntime;
        }
        public async Task<bool> IsUserLoggedIn()
        {
            string cookie = await _jsRuntime.InvokeAsync<string>("getCookie", "JSESSIONID");
            bool completed = !string.IsNullOrWhiteSpace(cookie);
            return completed;
        }
    }
}
