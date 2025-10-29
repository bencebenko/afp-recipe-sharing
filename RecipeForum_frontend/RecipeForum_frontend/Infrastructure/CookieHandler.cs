using Microsoft.AspNetCore.Components.WebAssembly.Http;

namespace RecipeForum_frontend.Infrastructure
{
    public class CookieHandler : DelegatingHandler
    {
        protected override async Task<HttpResponseMessage>
        SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            request.SetBrowserRequestCredentials(BrowserRequestCredentials.Include);

            return await base.SendAsync(request, cancellationToken);
        }
    }
}
