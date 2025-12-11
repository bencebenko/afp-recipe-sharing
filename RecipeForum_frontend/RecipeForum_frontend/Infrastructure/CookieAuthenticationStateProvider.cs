using Microsoft.AspNetCore.Components.Authorization;
using RecipeForum_frontend.Generated;
using System.Security.Claims;

namespace RecipeForum_frontend.Infrastructure
{
    public class CookieAuthenticationStateProvider : AuthenticationStateProvider
    {
        private readonly SwaggerClient _apiClient;

        public CookieAuthenticationStateProvider(SwaggerClient apiClient)
        {
            _apiClient = apiClient;
        }

        public override async Task<AuthenticationState> GetAuthenticationStateAsync()
        {
            try
            {
                var user = await _apiClient.GetCurrentUserAsync();

                var identity = new ClaimsIdentity(new[]
                {
                new Claim(ClaimTypes.Name, user.UserName),
                new Claim(ClaimTypes.Role, "USER")
            }, "cookie-auth");

                return new AuthenticationState(new ClaimsPrincipal(identity));
            }
            catch (ApiException ex) when (ex.StatusCode == 401)
            {
                return new AuthenticationState(new ClaimsPrincipal(new ClaimsIdentity()));
            }
        }

        public async Task MarkUserAsLoggedOut()
        {
            // új auth state
            var anon = new AuthenticationState(new ClaimsPrincipal(new ClaimsIdentity()));
            NotifyAuthenticationStateChanged(Task.FromResult(anon));
        }

        public async Task MarkUserAsLoggedIn()
        {
            var state = await GetAuthenticationStateAsync();
            NotifyAuthenticationStateChanged(Task.FromResult(state));
        }

        public void NotifyAuthenticationStateChanged()
        {
            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }
}
