import {Component, EventEmitter, Input, Output, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {Report} from "./report";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {MdDialog} from "@angular/material";
import {ConfirmDeleteDialogComponent} from "../../../support/confirm-delete-dialog.component";
import {ReportDataService} from "./report.data.service";

@Component({
  selector: 'reports',
  templateUrl: './report.component.html'
})
export class ReportComponent {

  @Input() header = "Raporty...";
  // list is paginated
  currentPage: PageResponse<Report> = new PageResponse<Report>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: Report = new Report();
  /** When 'sub' is true, it means this list is used as a one-to-many list.
   * It belongs to a parent entity, as a result the addNew operation
   * must prefill the parent entity. The prefill is not done here, instead we
   * emit an event. When 'sub' is false, we display basic search criterias
   */
  @Input() sub: boolean;
  @Output() onAddNewClicked = new EventEmitter();

  msgs: Message[] = [];

  constructor(private router: Router,
              private reportDataService: ReportDataService,
              private confirmDeleteDialog: MdDialog) {
  }

  showDeleteDialog(rowData: any) {
    let reportToDelete: Report = <Report> rowData;

    let dialogRef = this.confirmDeleteDialog.open(ConfirmDeleteDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        this.delete(reportToDelete);
      }
    });
  }

  private delete(reportToDelete: Report) {
    let id = reportToDelete.id;

    this.msgs = []; //this line fix disappearing of messages
    this.reportDataService.delete(id).subscribe(
      response => {
        this.currentPage.remove(reportToDelete);
        this.updateVisibility();
      },
      error => this.msgs.push({severity:'error', summary:'Nie można usunąć!', detail: error})
    );
  }

  /**
   * When used as a 'sub' component (to display one-to-many list), refreshes the table
   * content when the input changes.
   */
  ngOnChanges(changes: SimpleChanges) {
    this.loadPage({first: 0, rows: 10, sortField: null, sortOrder: null, filters: null, multiSortMeta: null});
  }

  /**
   * Invoked automatically by primeng datatable.
   */
  loadPage(event: LazyLoadEvent) {
    this.msgs = []; //this line fix disappearing of messages
    this.reportDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  addNew() {
    if (this.sub) {
      this.onAddNewClicked.emit("addNew");
    } else {
      this.router.navigate(['/reports/add']);
    }
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reports', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}
