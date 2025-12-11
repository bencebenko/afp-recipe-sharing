using Microsoft.AspNetCore.Components.Authorization;
using RecipeForum_frontend.Infrastructure.Interfaces;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

namespace RecipeForum_frontend.Infrastructure
{
    public class RequestService : IRequestService
    {
        private readonly HttpClient _client;
        private readonly AuthenticationStateProvider _cookieProvider;
        public RequestService(HttpClient client, AuthenticationStateProvider cookieProvider)
        {
            _client = client;
            _cookieProvider = cookieProvider;
        }

        public async Task<bool> LoginAsync(string username, string password)
        {
            _client.DefaultRequestHeaders.Add("Accept", "*/*");
            _client.DefaultRequestHeaders.Add("Connection", "keep-alive");
            var content = new { usernname = username, password = password };
            var response = await _client.PostAsJsonAsync("http://localhost:8080/login", content);
            response.EnsureSuccessStatusCode();
            return response.IsSuccessStatusCode;
        }

        public async Task LogoutAsync()
        {
            var response = await _client.PostAsync("http://localhost:8080/logout", null);
            response.EnsureSuccessStatusCode();
        }
    }
}
