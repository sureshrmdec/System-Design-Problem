/**
Implement a rate limiter, provide one method: is_ratelimited(timestamp, event, rate, increment).

timestamp: The current timestamp, which is an integer and in second unit.
event: The string to distinct different event. for example, "login" or "signup".
rate: The rate of the limit. 1/s (1 time per second), 2/m (2 times per minute), 10/h (10 times per hour), 100/d (100 times per day). The format is [integer]/[s/m/h/d].
increment: Whether we should increase the counter. (or take this call as a hit of the given event)
The method should return true or false to indicate the event is limited or not.

Example
is_ratelimited(1, "login", "3/m", true), return false.
is_ratelimited(11, "login", "3/m", true), return false.
is_ratelimited(21, "login", "3/m", true), return false.
is_ratelimited(30, "login", "3/m", true), return true.
is_ratelimited(65, "login", "3/m", true), return false.
is_ratelimited(300, "login", "3/m", true), return false.

*/
public class RateLimiter {
    /**
     * @param timestamp the current timestamp
     * @param event the string to distinct different event
     * @param rate the format is [integer]/[s/m/h/d]
     * @param increment whether we should increase the counter
     * @return true or false to indicate the event is limited or not
     */
    public boolean isRatelimited(int timestamp, String event, String rate, boolean increment) {
        // Write your code here
        int start = rate.indexOf("/");
        int total_time = Integer.parseInt(rate.substring(0, start));
        String type = rate.substring(start + 1, rate.length());

        int time = 1;
        if (type.equals("m"))
            time = time * 60;
        else if (type.equals("h"))
            time = time * 60 * 60;
        else if (type.equals("d"))
            time = time * 60 * 60 * 24;
        int last_time = timestamp - time + 1;

        if (!map.containsKey(event))
            map.put(event, new ArrayList<Integer>());

        boolean rt = find_event(map.get(event), last_time) >= total_time;
        if (increment && !rt)
            insert_event(map.get(event), timestamp);
        return rt;
    }

    public void insert_event(List<Integer> event, int timestamp) {
        event.add(timestamp);
    }

    public int find_event(List<Integer> event, int last_time) {
        int l = 0, r = event.size() - 1;
        if (r == -1)
            return 0;
        if (event.get(r) < last_time)
            return 0;
        int ans = 0;
        while (l <= r) {
            int mid = (l + r) >> 1;
            if (event.get(mid) >= last_time) {
                ans = mid;
                r = mid - 1;
            } else
                l = mid + 1;
        }
        return event.size() - 1 - ans + 1;
    }

    private HashMap<String, List<Integer>> map = new HashMap<String , List<Integer>>();
}