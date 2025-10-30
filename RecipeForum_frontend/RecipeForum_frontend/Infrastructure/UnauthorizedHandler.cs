using Microsoft.AspNetCore.Components;
using System.Net;

namespace RecipeForum_frontend.Infrastructure
{
    public class UnauthorizedHandler : DelegatingHandler
    {
        private readonly NavigationManager _navigationManager;

        public UnauthorizedHandler(NavigationManager navigationManager)
        {
            _navigationManager = navigationManager;
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            var response = await base.SendAsync(request, cancellationToken);
            if (response.StatusCode == HttpStatusCode.Unauthorized)
            {
                _navigationManager.NavigateTo("http://localhost:8080/login");
            }
            return response;
        }
    }
}
