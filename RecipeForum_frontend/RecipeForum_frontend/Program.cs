using Blazored.LocalStorage;
using RecipeForum_frontend.Generated;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.Extensions.DependencyInjection;
using MudBlazor;
using MudBlazor.Services;
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
            builder.Services.AddScoped<ITokenService, TokenService>();

            builder.Services.AddMudServices(configuration =>
            {
                configuration.SnackbarConfiguration.PositionClass = Defaults.Classes.Position.TopRight;
                configuration.SnackbarConfiguration.HideTransitionDuration = 100;
                configuration.SnackbarConfiguration.ShowTransitionDuration = 100;
                configuration.SnackbarConfiguration.VisibleStateDuration = 1000;
                configuration.SnackbarConfiguration.ShowCloseIcon = true;
            });

            builder.Services.AddTransient<AuthHeaderHandler>();
            builder.Services.AddHttpClient("RecipeForum", client =>
            {
                client.DefaultRequestHeaders.AcceptLanguage.Clear();
                client.DefaultRequestHeaders.AcceptLanguage.ParseAdd(CultureInfo.DefaultThreadCurrentCulture?.TwoLetterISOLanguageName);
                client.BaseAddress = new Uri("http://127.0.0.1:5180");
                client.Timeout = TimeSpan.FromSeconds(1);
            }).AddHttpMessageHandler<AuthHeaderHandler>();

            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddSingleton<MudLocalizer, MudLocalizer>();
            builder.Services.AddHttpClientInterceptor();
            builder.Services.AddScoped<SwaggerClient>(
                sp => new SwaggerClient(sp.GetRequiredService<IHttpClientFactory>()
                .CreateClient("RecipeForum")
                .EnableIntercept(sp))
            );

            builder.Services.AddBlazorContextMenu();

            await builder.Build().RunAsync();
        }
    }
}
