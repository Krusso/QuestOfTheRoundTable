
namespace Card
{
    public class QuestCard : StoryCard
    {
        public int Stages { get; set; }
        public string Foe { get; set; }

        public QuestCard(string name, int stages, string foe) : base(name, StoryType.Quest)
        {
            Stages = stages;
            Foe = foe;
        }
    }
}

