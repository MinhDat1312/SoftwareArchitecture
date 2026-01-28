package iuh.fit.DesignPattern.observer;

public class TeamMember implements Observer {

    private final String name;

    public TeamMember(String name) {
        this.name = name;
    }

    @Override
    public void update(Task task) {
        System.out.println(
                "🔔 " + name + " nhận thông báo: Task '"
                        + task.getTitle() + "' chuyển sang trạng thái "
                        + task.getStatus()
        );
    }
}

