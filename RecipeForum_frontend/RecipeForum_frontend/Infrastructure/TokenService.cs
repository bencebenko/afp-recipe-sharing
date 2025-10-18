using RecipeForum_frontend.Infrastructure.Interfaces;
using RecipeForum_frontend.Infrastructure.Models;
using static RecipeForum_frontend.Constants.Constants;
using System.Net.Http.Json;
using System.Text.Json;
using Blazored.LocalStorage;

namespace RecipeForum_frontend.Infrastructure
{
    public class TokenService : ITokenService
    {
        private readonly HttpClient _http;
        private readonly SemaphoreSlim _refreshLock = new SemaphoreSlim(1, 1);
        private readonly ILocalStorageService _localStorageService;

        public TokenService(HttpClient http, ILocalStorageService localStorage)
        {
            _http = http;
            _localStorageService = localStorage;
        }

        private async Task<(string? access, string? refresh, DateTime? expiry)> ReadStoredAsync()
        {
            var accesToken = await _localStorageService.GetItemAsStringAsync("AccessToken");
            var refreshToken = await _localStorageService.GetItemAsStringAsync("RefreshToken");
            var expiry = DateTime.Parse(await _localStorageService.GetItemAsStringAsync("Expiry"));
            return (accesToken, refreshToken, expiry);
        }

        private async Task StoreAsync(string access, string? refresh, DateTime expiry)
        {
            await _localStorageService.SetItemAsStringAsync("AccessToken", access);
            await _localStorageService.SetItemAsStringAsync("RefreshToken", refresh);
            await _localStorageService.SetItemAsStringAsync("Expiry", expiry.ToString());
        }

        public async Task<string?> GetAccessTokenAsync()
        {
            var (access, refresh, expiry) = await ReadStoredAsync();
            if (access == null) return null;
            if (expiry.HasValue && expiry.Value <= DateTime.UtcNow.AddSeconds(30))
            {
                await EnsureFreshTokenAsync();
                (access, _, _) = await ReadStoredAsync();
            }
            return access;
        }

        public async Task EnsureFreshTokenAsync()
        {
            await _refreshLock.WaitAsync();
            try
            {
                var (access, refresh, expiry) = await ReadStoredAsync();
                if (access != null && expiry.HasValue && expiry.Value > DateTime.UtcNow.AddSeconds(30))
                {
                    return; // someone else already refreshed
                }

                if (string.IsNullOrEmpty(refresh))
                {
                    await ClearTokensAsync();
                    return;
                }

                // Majd SwaggerClient koddal.

                //var refreshReq = new { refresh_token = refresh };
                //var resp = await _http.PostAsJsonAsync("auth/refresh", refreshReq);
                //if (!resp.IsSuccessStatusCode)
                //{
                //    await ClearTokensAsync();
                //    return;
                //}

                //var tokenResp = await resp.Content.ReadFromJsonAsync<TokenResponse>();
                //if (tokenResp == null)
                //{
                //    await ClearTokensAsync();
                //    return;
                //}

                //var newExpiry = DateTime.UtcNow.AddSeconds(tokenResp.ExpiresIn);
                //await StoreAsync(tokenResp.AccessToken, tokenResp.RefreshToken ?? refresh, newExpiry);
            }
            finally
            {
                _refreshLock.Release();
            }
        }

        public async Task ClearTokensAsync()
        {
            await _localStorageService.SetItemAsStringAsync(AuthTokenName, "");
        }
    }
}
