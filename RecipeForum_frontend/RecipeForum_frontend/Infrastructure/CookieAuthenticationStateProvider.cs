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
                var identity = new ClaimsIdentity( "cookie-auth");
                return new AuthenticationState(new ClaimsPrincipal(identity));
            }
            catch (RecipeForum_frontend.Generated.ApiException ex) when (ex.StatusCode == 401)
            {
                return new AuthenticationState(new ClaimsPrincipal(new ClaimsIdentity()));
            }
        }

        public void NotifyAuthenticationStateChanged()
        {
            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }
}
