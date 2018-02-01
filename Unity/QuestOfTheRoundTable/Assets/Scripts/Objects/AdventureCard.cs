namespace Card
{
    public enum AdventureType
    {
        Foe, Weapon, Ally, Armour, Test
    }
    public class AdventureCard : Card
    {
        public int BattlePoints { get; set; }
        public int NamedBattlePoints { get; set; }
        public AdventureCard(string name): base(name)
        {
        }
        public AdventureCard(string name, int bp) : base(name)
        {
            BattlePoints = bp;
        }
        public AdventureCard(string name, int bp, int nbp) : base(name)
        {
            BattlePoints = bp;
            NamedBattlePoints = nbp;
        }
    }
}

