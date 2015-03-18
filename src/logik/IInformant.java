package logik;

public interface IInformant {
	void register(IListener l);
	void unregister(IListener l);
}
