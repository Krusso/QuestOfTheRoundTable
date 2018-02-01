
namespace Card
{
    public enum Rank
    {
        Squire, Knight, Champion
    }
    public class RankCard : Card
    {
        public int CurrentShields { get; set; }
        public RankCard(string name, int numShields) : base(name)
        {
            CurrentShields = numShields;
        }
    }
}

