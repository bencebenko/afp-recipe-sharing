using System;
using Microsoft.AspNetCore.Components;

namespace RecipeForum_frontend.Infrastructure
{
    public class SearchState
    {

        private string _currentSearchTerm = string.Empty;

        public string CurrentSearchTerm
        {
            get => _currentSearchTerm;
            private set
            {
                if (_currentSearchTerm != value)
                {
                    _currentSearchTerm = value;
                    NotifyStateChanged();
                }
            }
        }

        public void SetSearchTerm(string newTerm)
        {
            CurrentSearchTerm = newTerm;
        }

        public event Action? OnChange;

        private void NotifyStateChanged() => OnChange?.Invoke();
    }
}

