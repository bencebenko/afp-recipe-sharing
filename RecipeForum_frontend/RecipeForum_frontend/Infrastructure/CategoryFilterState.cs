namespace RecipeForum_frontend.Infrastructure
{
    public class CategoryFilterState
    {
        public event Action OnChange;
        public event Action OnFilterReset;
        private bool _showFilter = false;

        public bool ShowFilter
        {
            get => _showFilter;
            set
            {
                if (_showFilter != value)
                {
                    _showFilter = value;
                    NotifyStateChanged();
                }
            }
        }
        public void ToggleFilter()
        {
            ShowFilter = !ShowFilter;
        }
        private void NotifyStateChanged() => OnChange?.Invoke();
        public void ResetFilter()
        {
            ShowFilter = false;
            OnFilterReset?.Invoke();
        }
    }
}
