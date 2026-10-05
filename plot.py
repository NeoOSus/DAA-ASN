import csv
from pathlib import Path
import matplotlib.pyplot as plt

folder = Path(__file__).resolve().parent / "results"
with (folder / "results.csv").open() as file:
    rows = list(csv.DictReader(file))

for workload in ["W1", "W2", "W3", "W4"]:
    fig, axes = plt.subplots(2, 2, figsize=(10, 4.8))
    axes = axes.ravel()
    variants = ["head", "middle"] if workload == "W3" else ["-"]
    structures = ["MinHeap"] if workload == "W4" else ["DynamicArray", "MyLinkedList"]
    for structure in structures:
        for variant in variants:
            selected = [r for r in rows if r["workload"] == workload
                        and r["structure"] == structure and r["variant"] == variant]
            selected.sort(key=lambda r: int(r["n"]))
            label = structure + (" " + variant if variant != "-" else "")
            for ax, metric in zip(axes, ["time_ms", "steps", "moves", "comparisons"]):
                ax.plot([int(r["n"]) for r in selected],
                        [float(r[metric]) for r in selected],
                        marker="o", linestyle="--" if variant == "middle" else "-", label=label)
                ax.set_xscale("log")
                # symlog keeps zero counters visible.
                ax.set_yscale("symlog", linthresh=0.001 if metric == "time_ms" else 1)
                ax.set_xlabel("n (elements)", fontsize=9)
                ax.set_ylabel("Time (ms)" if metric == "time_ms" else metric + " (count)", fontsize=9)
                ax.tick_params(labelsize=8)
                ax.grid(True, alpha=0.3)
    for ax in axes:
        values = [v for line in ax.lines for v in line.get_ydata()]
        if min(values) > 0:
            ax.set_yscale("log")
        else:
            ax.set_ylim(bottom=0)
            if max(values) == 0:
                ax.set_ylim(0, 1)
        ax.legend(fontsize=7)
    fig.suptitle(workload + ": median time and counters (log scales; y includes zero)", fontsize=11)
    fig.tight_layout()
    (folder / "plots").mkdir(exist_ok=True)
    fig.savefig(folder / "plots" / (workload + ".png"), dpi=160)
    plt.close(fig)
print("Saved four charts in results/plots/")
