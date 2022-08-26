package enigma;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Nitin Nazeer
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments "
                    + "allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        if (!(_input.hasNext("\\*"))) {
            throw error("empty file");
        }
        Machine machine = readConfig();

        while (_input.hasNext()) {
            while (_input.hasNext("\\*")) {
                String setting = _input.nextLine();
                while (setting.equals("")) {
                    _output.println();
                    setting = _input.nextLine();
                }
                setUp(machine, setting);
            }
            for (int i = 1; i < machine.numRotors() - machine.numPawls(); i++) {
                if (machine.getRotor(i).rotates()) {
                    throw error("moving rotor in wrong position");
                }
            }

            String inputLine;
            inner:
            {
                while (_input.hasNextLine()) {
                    if (_input.hasNext("\\*")) {
                        break inner;
                    }
                    inputLine = _input.nextLine().replaceAll(" ", "");
                    String converted = machine.convert(inputLine);
                    printMessageLine(converted);
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();

            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();

            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            if (!(name.matches("\\w+"))) {
                throw error("invalid input file");
            }
            String desc = _config.next();
            String cycles = "";
            String format = "(\\([\\w._]*\\))*";

            while (_config.hasNext(format)) {
                cycles += _config.next();
            }

            Permutation perm = new Permutation(cycles, _alphabet);

            char type = desc.charAt(0);
            if (type == 'M') {
                String notches = desc.substring(1);
                return new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }


    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            Scanner setter = new Scanner(settings);
            if (!(setter.next().equals("*"))) {
                throw error("Invalid input start, needs *");
            }

            String[] rotorNames = new String[M.numRotors()];
            for (int i = 0; i < M.numRotors(); i += 1) {
                rotorNames[i] = setter.next();
            }

            String setting = setter.next();
            M.insertRotors(rotorNames);

            if (setting.length() != M.numRotors() - 1) {
                throw error("bad settings length");
            }

            M.setRotors(setting);



            if (setter.hasNext("\\w+")) {
                String ringSetting = setter.next();
                M.setRings(ringSetting);
            }

            String plugCycles = "";
            while (setter.hasNext("\\(\\w*\\)")) {
                plugCycles += setter.next();
            }
            Permutation plugPerm = new Permutation(plugCycles, _alphabet);

            M.setPlugboard(plugPerm);
        } catch (NoSuchElementException excp) {
            throw error("bad rotor settings");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    public void printMessageLine(String msg) {
        if (msg.length() <= 5) {
            _output.println(msg);
        } else {
            _output.print(msg.substring(0, 5) + " ");
            printMessageLine(msg.substring(5));
        }

    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
