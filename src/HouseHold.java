public class HouseHold
{
    private int id;
    private double income;
    private int members;
    private String state;

    // Constructor
    public HouseHold(int id, double income, int members, String state)
    {
        this.id = id;
        this.income = income;
        this.members = members;
        this.state = state;
    }

    // Getters
    public int getId()
    {
        return id;
    }

    public double getIncome()
    {
        return income;
    }

    public int getMembers()
    {
        return members;
    }

    public String getState()
    {
        return state;
    }

    // Method to compute poverty level based on state and members
    public double getPovertyLevel2024()
    {
        // povertyLevel = base + (increment * (m - 2)), depending on state:
        // 48 contiguous states + DC: povertyLevel = 20440.00 + 5380.00 * (m - 2)
        // Alaska: povertyLevel = 25540.00 + 6730.00 * (m - 2)
        // Hawaii: povertyLevel = 23500.00 + 6190.00 * (m - 2)
        int m = this.members;
        double povertyLevel = 0.0;
        String st = this.state.toLowerCase();

        if(st.equals("alaska"))
        {
            if(m == 1)
                povertyLevel = 25540.00;  // If m=1, formula reduces to just the base since (m-2) = -1
            else if(m == 2)
                povertyLevel = 25540.00;
            else
                povertyLevel = 25540.00 + 6730.00 * (m - 2);
        }
        else if(st.equals("hawaii"))
        {
            if(m == 1)
                povertyLevel = 23500.00;
            else if(m == 2)
                povertyLevel = 23500.00;
            else
                povertyLevel = 23500.00 + 6190.00 * (m - 2);
        }
        else
        {
            // 48 contiguous + DC
            if(m == 1)
                povertyLevel = 20440.00;
            else if(m == 2)
                povertyLevel = 20440.00;
            else
                povertyLevel = 20440.00 + 5380.00 * (m - 2);
        }

        return povertyLevel;
    }
}