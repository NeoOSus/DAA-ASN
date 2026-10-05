import csv
from pathlib import Path
import matplotlib.pyplot as plt

folder = Path(__file__).resolve().parent / "results"
with (folder / "results.csv").open() as file:
    rows = list(csv.DictReader(file))

for workload in ["W1", "W2", "W3", "W4"]:
    fig, axes = plt.subplots(1, 4, figsize=(14, 3.3))
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
                ax.set_xlabel("n (elements)")
                ax.set_ylabel("Time (ms)" if metric == "time_ms" else metric + " (count)")
                ax.grid(True, alpha=0.3)
    for ax in axes:
        ax.legend(fontsize=6)
    fig.suptitle(workload + ": median time and operation counters (log scales; y includes zero)")
    fig.tight_layout()
    (folder / "plots").mkdir(exist_ok=True)
    fig.savefig(folder / "plots" / (workload + ".png"), dpi=160)
    plt.close(fig)
print("Saved four charts in results/plots/")
