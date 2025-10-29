using Microsoft.AspNetCore.Components.Authorization;
using RecipeForum_frontend.Generated;
using System.Security.Claims;

namespace RecipeForum_frontend.Infrastructure
{
    public class CookieAuthenticationStateProvider : AuthenticationStateProvider
    {
        private readonly SwaggerClient _apiClient;
        private bool _isAuthenticated = false;

        public CookieAuthenticationStateProvider(SwaggerClient apiClient)
        {
            _apiClient = apiClient;
        }

        public override async Task<AuthenticationState> GetAuthenticationStateAsync()
        {
            if (_isAuthenticated)
            {
                var cachedIdentity = new ClaimsIdentity("cookie-auth");
                return new AuthenticationState(new ClaimsPrincipal(cachedIdentity));
            }

            try
            {
                var identity = new ClaimsIdentity(new[]
                {
                    new Claim(ClaimTypes.Role, "User")
                }, "cookie-auth");

                _isAuthenticated = true; // Mark as authenticated
                return new AuthenticationState(new ClaimsPrincipal(identity));
            }
            catch (ApiException ex) when (ex.StatusCode == 401)
            {
                _isAuthenticated = false;
                return new AuthenticationState(new ClaimsPrincipal(new ClaimsIdentity()));
            }
        }

        public void NotifyAuthenticationStateChanged()
        {
            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }
}
