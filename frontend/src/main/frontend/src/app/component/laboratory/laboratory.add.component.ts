import {Component, EventEmitter, Input, OnDestroy, Output} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {Laboratory} from "../laboratory/laboratory";
import {Report} from "../report/report";
import {LaboratoryDataService} from "./laboratory.data.service";

@Component({
  templateUrl: 'laboratory.add.component.html',
  selector: 'laboratories-add',
})
export class LaboratoryAddComponent implements OnDestroy {

  @Input() header = "Sprawozdania z tego laboratorium...";
  laboratory : Laboratory;
  laboratoryReports: Report[] = [];

  private params_subscription: any;

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<Laboratory>();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private laboratoryDataService: LaboratoryDataService) {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for Laboratory-add ' + id);
      if (id === 'add') {
        this.laboratory = new Laboratory();

      } else {
        this.laboratoryDataService.getLaboratory(id)
          .subscribe(laboratory => {
              this.laboratory = laboratory;
              this.laboratoryReports = laboratory.reports;
            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onSave() {
    this.laboratoryDataService.update(this.laboratory).
    subscribe(
      laboratory => {
        this.laboratory = laboratory;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.laboratory);
        } else {
          this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})
        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reports', id]);
  }
}
