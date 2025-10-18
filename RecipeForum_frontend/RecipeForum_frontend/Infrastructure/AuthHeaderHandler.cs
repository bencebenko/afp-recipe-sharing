using Blazored.LocalStorage;
using Microsoft.VisualBasic;
using RecipeForum_frontend.Infrastructure.Interfaces;
using System.Net;
using System.Net.Http.Headers;
using static RecipeForum_frontend.Constants.Constants;

namespace RecipeForum_frontend.Infrastructure
{
        

    public class AuthHeaderHandler : DelegatingHandler
    {
        private readonly ITokenService _tokenService;
        public AuthHeaderHandler(ITokenService tokenService)
        {
            _tokenService = tokenService;
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            // Attach access token
            var access = await _tokenService.GetAccessTokenAsync();
            if (!string.IsNullOrEmpty(access))
            {
                request.Headers.Authorization = new AuthenticationHeaderValue(HeaderBearer, access);
            }

            var response = await base.SendAsync(request, cancellationToken);

            if (response.StatusCode == HttpStatusCode.Unauthorized)
            {
                // Try refresh and retry once
                await _tokenService.EnsureFreshTokenAsync();
                var newAccess = await _tokenService.GetAccessTokenAsync();
                if (!string.IsNullOrEmpty(newAccess))
                {
                    // clone request (HttpRequestMessage can't be sent twice). Create a copy:
                    var newRequest = await CloneHttpRequestMessageAsync(request);
                    newRequest.Headers.Authorization = new AuthenticationHeaderValue(HeaderBearer, newAccess);
                    response.Dispose();
                    return await base.SendAsync(newRequest, cancellationToken);
                }
            }

            return response;
        }

        private async Task<HttpRequestMessage> CloneHttpRequestMessageAsync(HttpRequestMessage req)
        {
            var clone = new HttpRequestMessage(req.Method, req.RequestUri!);

            // copy headers
            foreach (var header in req.Headers)
                clone.Headers.TryAddWithoutValidation(header.Key, header.Value);

            // copy content
            if (req.Content != null)
            {
                var ms = new MemoryStream();
                await req.Content.CopyToAsync(ms);
                ms.Position = 0;
                clone.Content = new StreamContent(ms);

                if (req.Content.Headers != null)
                    foreach (var h in req.Content.Headers)
                        clone.Content.Headers.TryAddWithoutValidation(h.Key, h.Value);
            }
            return clone;
        }
        
    }
    
}
