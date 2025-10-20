using Blazored.LocalStorage;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.Extensions.DependencyInjection;
using MudBlazor;
using MudBlazor.Services;
using RecipeForum_frontend.Generated;
using RecipeForum_frontend.Infrastructure;
using RecipeForum_frontend.Infrastructure.Interfaces;
using System.Globalization;
using System.Net.Http;
using Toolbelt.Blazor.Extensions.DependencyInjection;

namespace RecipeForum_frontend
{
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebAssemblyHostBuilder.CreateDefault(args);
            builder.RootComponents.Add<App>("#app");
            builder.RootComponents.Add<HeadOutlet>("head::after");
            builder.Services.AddBlazoredLocalStorage();

            builder.Services.AddMudServices(configuration =>
            {
                configuration.SnackbarConfiguration.PositionClass = Defaults.Classes.Position.TopRight;
                configuration.SnackbarConfiguration.HideTransitionDuration = 100;
                configuration.SnackbarConfiguration.ShowTransitionDuration = 100;
                configuration.SnackbarConfiguration.VisibleStateDuration = 1000;
                configuration.SnackbarConfiguration.ShowCloseIcon = true;
            });

            builder.Services.AddHttpClient<SwaggerClient>(client =>
            {
                client.BaseAddress = new Uri("http://localhost:8080"); 
            });

            builder.Services.AddAuthorizationCore();
            builder.Services.AddScoped<AuthenticationStateProvider, CookieAuthenticationStateProvider>();
            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddSingleton<MudLocalizer, MudLocalizer>();
            builder.Services.AddBlazorContextMenu();

            await builder.Build().RunAsync();
        }
    }
}
