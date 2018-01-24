
using System.Collections;

public class Player {

    public Card.Rank Rank { get;set; }
    public ArrayList Hand;

    public Player()
    {
        Rank = Card.Rank.Squire;
        Hand = new ArrayList();
    }
    public void RankUp()
    {
        if(Rank == Card.Rank.Squire)
        {
            Rank = Card.Rank.Knight;
        }else if(Rank == Card.Rank.Knight)
        {
            Rank = Card.Rank.Champion;
        }
    }
    public void AddCard(Card.AdventureCard c)
    {
        Hand.Add(c);
    }
    public string DisplayHand()
    {
        return Hand.ToString();
    }
}
