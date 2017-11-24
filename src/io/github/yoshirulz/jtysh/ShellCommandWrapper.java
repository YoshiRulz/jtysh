package io.github.yoshirulz.jtysh;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.yoshirulz.jtysh.JTyshInternalError.CannotFinishTempfileRead;
import static java.lang.ProcessBuilder.Redirect;
import static java.lang.ProcessBuilder.Redirect.PIPE;

/**
 * @author YoshiRulz
 * @version 2017-11-21/00
 */
@SuppressWarnings("UseOfProcessBuilder")
public class ShellCommandWrapper {
	@SuppressWarnings({"HardCodedStringLiteral", "SpellCheckingInspection"})
	private static final String TEMP_PREFIX = "jtysh-";

	private final File tempFile;
	private String[] output;

	public ShellCommandWrapper(ArrayList<String> cmdPath, boolean ignoreError, long timeout, TimeUnit timeoutUnits) throws IOException, InterruptedException {
		tempFile = File.createTempFile(TEMP_PREFIX, null);
		Redirect toTempFile = Redirect.to(tempFile);
		ProcessBuilder pb = new ProcessBuilder(cmdPath)
			.redirectOutput(toTempFile).redirectError(ignoreError ? PIPE : toTempFile);
		execute(pb, timeout, timeoutUnits);
	}
	public ShellCommandWrapper(String[] cmdPath, boolean ignoreError, long timeout, TimeUnit timeoutUnits) throws IOException, InterruptedException {
		tempFile = File.createTempFile(TEMP_PREFIX, null);
		Redirect toTempFile = Redirect.to(tempFile);
		ProcessBuilder pb = new ProcessBuilder(cmdPath)
			.redirectOutput(toTempFile).redirectError(ignoreError ? PIPE : toTempFile);
		execute(pb, timeout, timeoutUnits);
	}

	public static ShellCommandWrapper withLookup(String program, String[] args, boolean ignoreError, long timeout, TimeUnit timeoutUnits) throws WhichWrapper.ProgramNotFoundException, IOException, InterruptedException {
		String[] temp = new String[args.length + 1];
		temp[0] = new WhichWrapper(program).path;
		System.arraycopy(args, 0, temp, 1, args.length);
		return new ShellCommandWrapper(temp, ignoreError, timeout, timeoutUnits);
	}

	@SuppressWarnings({"ArrayLengthInLoopCondition", "MethodCallInLoopCondition", "CollectionWithoutInitialCapacity", "ImplicitDefaultCharsetUsage"})
	private void execute(ProcessBuilder pb, long timeout, TimeUnit timeoutUnits) throws IOException, InterruptedException {
		Process p = pb.start();
		p.waitFor(timeout, timeoutUnits);

		Reader r = new FileReader(tempFile);
		List<String> temp = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(r)) {
			while (br.ready()) temp.add(br.readLine());
		} catch (IOException ignored) {
			Main.error(CannotFinishTempfileRead);
		} finally {
			r.close();
			output = new String[temp.size()];
			for (int i = 0; i < output.length; i++) output[i] = temp.get(i);
		}
	}

	public final String[] getOutput() {
		return output.clone();
	}
}