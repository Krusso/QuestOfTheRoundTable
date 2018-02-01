namespace Card
{
    public class FoeCard : AdventureCard
    {
        public FoeCard(string name) : base(name)
        {
        }
        public FoeCard(string name, int bp) : base(name)
        {
            BattlePoints = bp;
        }
        public FoeCard(string name, int bp, int nbp) : base(name)
        {
            BattlePoints = bp;
            NamedBattlePoints = nbp;
        }
    }
}

