namespace Card
{
    public class StoryCard : Card
    {
        public enum StoryType
        {
            Quest, Event, Tournament
        }
        protected StoryType Type { get; set; }
       // protected string Description { get; set; }
        public StoryCard()
        {

        }
        public StoryCard(string name, StoryType type) : base(name)
        {
            Type = type;
        }
    }

}
