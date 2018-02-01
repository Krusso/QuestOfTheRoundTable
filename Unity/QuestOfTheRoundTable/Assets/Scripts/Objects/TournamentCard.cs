
namespace Card
{
    public class TournamentCard : StoryCard
    {
        public int Shields { get; set; }
        public TournamentCard(string name, int shields) : base(name, StoryType.Tournament)
        {
            Shields = shields;
        }
    }
}
